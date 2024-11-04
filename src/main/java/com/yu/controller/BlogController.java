package com.yu.controller;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.yu.annotation.AuthCheck;
import com.yu.common.BaseResponse;
import com.yu.common.DeleteRequest;
import com.yu.common.ErrorCode;
import com.yu.common.ResultUtils;
import com.yu.constant.CommonConstant;
import com.yu.constant.UserConstant;
import com.yu.exception.BusinessException;
import com.yu.exception.ThrowUtils;
import com.yu.model.dto.tarticle.TArticleAddRequest;
import com.yu.model.dto.tarticle.TArticleQueryRequest;
import com.yu.model.dto.tarticle.TArticleUpdateRequest;
import com.yu.model.dto.tcategory.TCategoryAddRequest;
import com.yu.model.dto.tcategory.TCategoryQueryRequest;
import com.yu.model.dto.tcategory.TCategoryUpdateRequest;
import com.yu.model.entity.*;
import com.yu.model.vo.ArticleVO;
import com.yu.service.*;
import com.yu.utils.SqlUtils;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.ObjectUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.BeanUtils;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.util.CollectionUtils;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import java.util.HashMap;
import java.util.List;
import java.util.stream.Collectors;

/**
 * 博客模块
 */
@RestController
@RequestMapping("/blog")
@Slf4j
public class BlogController {

    @Resource
    private TCategoryService tcategoryService;

    @Resource
    private TArticleService tarticleService;

    @Resource
    private TArticleContentService tarticleContentService;

    @Resource
    private TArticleCategoryRelService tarticleCategoryRelService;

    @Resource
    private UserService userService;

    @Resource
    private StringRedisTemplate stringRedisTemplate;


    // region 分类接口-增删改查

    /**
     * 仅管理员可以创建分类
     *
     * @param tcategoryAddRequest
     * @param request
     * @return
     */
    @PostMapping("/tcategory/add")
    @AuthCheck(mustRole = UserConstant.ADMIN_ROLE)
    public BaseResponse<Long> addTCategory(@RequestBody TCategoryAddRequest tcategoryAddRequest, HttpServletRequest request) {
        if (tcategoryAddRequest == null) {
            throw new BusinessException(ErrorCode.PARAMS_ERROR);
        }
        TCategory tcategory = new TCategory();
        BeanUtils.copyProperties(tcategoryAddRequest, tcategory);
        User loginUser = userService.getLoginUser(request);
        boolean result = tcategoryService.save(tcategory);
        ThrowUtils.throwIf(!result, ErrorCode.OPERATION_ERROR);
        long newTCategoryId = tcategory.getId();
        return ResultUtils.success(newTCategoryId);
    }

    /**
     * 仅管理员可以删除分类
     *
     * @param deleteRequest
     * @param request
     * @return
     */
    @PostMapping("/tcategory/delete")
    @AuthCheck(mustRole = UserConstant.ADMIN_ROLE)
    public BaseResponse<Boolean> deleteTCategory(@RequestBody DeleteRequest deleteRequest, HttpServletRequest request) {
        if (deleteRequest == null || deleteRequest.getId() <= 0) {
            throw new BusinessException(ErrorCode.PARAMS_ERROR);
        }
        User user = userService.getLoginUser(request);
        long id = deleteRequest.getId();
        // 判断是否存在
        TCategory oldTCategory = tcategoryService.getById(id);
        ThrowUtils.throwIf(oldTCategory == null, ErrorCode.NOT_FOUND_ERROR);
        boolean b = tcategoryService.removeById(id);
        return ResultUtils.success(b);
    }

    /**
     * 更新分类（仅管理员）
     *
     * @param tcategoryUpdateRequest
     * @return
     */
    @PostMapping("/tcategory/update")
    @AuthCheck(mustRole = UserConstant.ADMIN_ROLE)
    public BaseResponse<Boolean> updateTCategory(@RequestBody TCategoryUpdateRequest tcategoryUpdateRequest) {
        if (tcategoryUpdateRequest == null || tcategoryUpdateRequest.getId() <= 0) {
            throw new BusinessException(ErrorCode.PARAMS_ERROR);
        }
        TCategory tcategory = new TCategory();
        BeanUtils.copyProperties(tcategoryUpdateRequest, tcategory);
        long id = tcategoryUpdateRequest.getId();
        // 判断是否存在
        TCategory oldTCategory = tcategoryService.getById(id);
        ThrowUtils.throwIf(oldTCategory == null, ErrorCode.NOT_FOUND_ERROR);
        boolean result = tcategoryService.updateById(tcategory);
        return ResultUtils.success(result);
    }

    /**
     * 分页获取分类列表（仅管理员可用）
     *
     * @param tCategoryQueryRequest
     * @return
     */
    @PostMapping("/tcategory/list/page")
    @AuthCheck(mustRole = UserConstant.ADMIN_ROLE)
    public BaseResponse<Page<TCategory>> listTCategoryByPage(@RequestBody TCategoryQueryRequest tCategoryQueryRequest) {
        long current = tCategoryQueryRequest.getCurrent();
        long size = tCategoryQueryRequest.getPageSize();
        // 查询数据库
        Page<TCategory> tcategoryPage = tcategoryService.page(new Page<>(current, size),
                tcategoryService.getQueryWrapper(tCategoryQueryRequest));
        return ResultUtils.success(tcategoryPage);
    }

    /**
     * 仅管理员根据 id 获取分类
     *
     * @param id
     * @return
     */
    @GetMapping("/tcategory/get")
    @AuthCheck(mustRole = UserConstant.ADMIN_ROLE)
    public BaseResponse<TCategory> getTCategoryById(long id, HttpServletRequest request) {
        if (id <= 0) {
            throw new BusinessException(ErrorCode.PARAMS_ERROR);
        }
        TCategory tcategory = tcategoryService.getById(id);
        if (tcategory == null) {
            throw new BusinessException(ErrorCode.NOT_FOUND_ERROR);
        }
        return ResultUtils.success(tcategory);
    }

    /**
     * 获取所有分类数据
     * @return
     */
    @PostMapping("/tcategory/list")
    public BaseResponse<List<String>> queryCategoryList() {
        QueryWrapper<TCategory> queryWrapper = new QueryWrapper<>();
        queryWrapper.select("name");
        List<String> list = tcategoryService.listObjs(queryWrapper, obj -> (String) obj);
//        List<TCategory> tCategories = tcategoryService.list();
//        List<TCategoryQueryRequest> list = null;
//        if (!CollectionUtils.isEmpty(tCategories)) {
//            list = tCategories.stream()
//                    .map(p -> TCategoryQueryRequest.builder()
//                            .id(p.getId())
//                            .name(p.getName())
//                            .build())
//                    .collect(Collectors.toList());
//        }

        return ResultUtils.success(list);
    }

    /**
     * 获取所属分类的文章分页数据
     * @param tcategoryQueryRequest
     * @return
     */
    @PostMapping("/tcategory/article/list")
    public BaseResponse<Page<TArticle>> queryArticlePageList(@RequestBody TCategoryQueryRequest tcategoryQueryRequest) {
        if (tcategoryQueryRequest == null) {
            throw new BusinessException(ErrorCode.PARAMS_ERROR);
        }
        long current = tcategoryQueryRequest.getCurrent();
        long size = tcategoryQueryRequest.getPageSize();
        String name = tcategoryQueryRequest.getName();

        // 限制爬虫
        ThrowUtils.throwIf(size > 20, ErrorCode.PARAMS_ERROR);

        Page<TArticle> tArticlePage;
        if(StringUtils.isNotBlank(name)){
            //select * from t_category where name = ? 根据名称获取分类id
            QueryWrapper<TCategory> wrapperTCategory = new QueryWrapper<>();
            wrapperTCategory.eq("name",name);
            TCategory category_id = tcategoryService.getOne(wrapperTCategory);
            ThrowUtils.throwIf(category_id == null, ErrorCode.NOT_FOUND_ERROR);
            //select * from t_article_category_rel where category_id = ? 根据分类id查询相关文章id
            QueryWrapper<TArticleCategoryRel> wrapperTArticleCategoryRel = new QueryWrapper<>();
            wrapperTArticleCategoryRel.eq( category_id.getId() != null,"category_id", category_id.getId());
            List<TArticleCategoryRel> tArticleCategoryRels = tarticleCategoryRelService.list(wrapperTArticleCategoryRel);
            // 判断该分类下是否存在文章
            if (CollectionUtils.isEmpty(tArticleCategoryRels)){
//            Page<ArticleVO> page = tarticleService.page(new Page<>(current, size));
                return  ResultUtils.success(null);
            }
            List<Long> article_ids = tArticleCategoryRels.stream().map(p -> p.getArticle_id()).collect(Collectors.toList());

            //select * from t_article where id = ? 根据文章id查询文章内容
            QueryWrapper<TArticle> wrapperTArticle = new QueryWrapper<>();
            wrapperTArticle.lambda().in(TArticle::getId, article_ids).orderByDesc(TArticle::getCreate_time);
            tArticlePage = tarticleService.page(new Page<>(current, size), wrapperTArticle);
        }else {
            System.out.println("为空");
            QueryWrapper<TArticle> wrapperTArticle = new QueryWrapper<>();
            wrapperTArticle.lambda().orderByDesc(TArticle::getCreate_time);
            tArticlePage = tarticleService.page(new Page<>(current, size), wrapperTArticle);
        }
        return ResultUtils.success(tArticlePage);


    }

    // endregion

    // region 文章接口-增删改查

    /**
     * 创建文章
     *
     * @param tarticleAddRequest
     * @param request
     * @return
     */
    @PostMapping("/tarticle/add")
    public BaseResponse<Long> addTArticle(@RequestBody TArticleAddRequest tarticleAddRequest, HttpServletRequest request) {
        if (tarticleAddRequest == null) {
            throw new BusinessException(ErrorCode.PARAMS_ERROR);
        }
        User loginUser = userService.getLoginUser(request);
        //文章
        TArticle tarticle = new TArticle();
        BeanUtils.copyProperties(tarticleAddRequest, tarticle);
        tarticle.setUserId(loginUser.getId());
        boolean result = tarticleService.save(tarticle);
        ThrowUtils.throwIf(!result, ErrorCode.OPERATION_ERROR);
        long tarticleId = tarticle.getId();
        //文章内容
        TArticleContent tarticleContent = new TArticleContent();
        tarticleContent.setContent(tarticleAddRequest.getContent());
        tarticleContent.setArticle_id(tarticleId);
        boolean saveContent = tarticleContentService.save(tarticleContent);
        ThrowUtils.throwIf(!saveContent,ErrorCode.OPERATION_ERROR);
        //所属分类
        TArticleCategoryRel tarticleCategoryRel = new TArticleCategoryRel();
        tarticleCategoryRel.setArticle_id(tarticleId);
        tarticleCategoryRel.setCategory_id(tarticleAddRequest.getCategoryId());
        boolean saveArticleCategoryRel = tarticleCategoryRelService.save(tarticleCategoryRel);
        ThrowUtils.throwIf(!saveArticleCategoryRel,ErrorCode.OPERATION_ERROR);

        return ResultUtils.success(tarticleId);
    }

    /**
     * 删除文章
     *
     * @param deleteRequest
     * @param request
     * @return
     */
    @PostMapping("/tarticle/delete")
    public BaseResponse<Boolean> deleteTArticle(@RequestBody DeleteRequest deleteRequest, HttpServletRequest request) {
        if (deleteRequest == null || deleteRequest.getId() <= 0) {
            throw new BusinessException(ErrorCode.PARAMS_ERROR);
        }
        User user = userService.getLoginUser(request);
        long id = deleteRequest.getId();
        // 判断是否存在
        TArticle oldTArticle = tarticleService.getById(id);
        ThrowUtils.throwIf(oldTArticle == null, ErrorCode.NOT_FOUND_ERROR);
        // 仅本人或管理员可删除
        if (!oldTArticle.getUserId().equals(user.getId()) && !userService.isAdmin(request)) {
            throw new BusinessException(ErrorCode.NO_AUTH_ERROR);
        }
        boolean b = tarticleService.removeById(id);
        // 删除文章内容
        // todo 文章有逻辑删除字段，但是没用，如果使用，以下的文章内容删除代码需删除
        // select * from t_article_content where article_id = ?
        QueryWrapper<TArticleContent> queryTArticleContent = new QueryWrapper<>();
        queryTArticleContent.eq("article_id",id);
        TArticleContent taricleContent = tarticleContentService.getOne(queryTArticleContent);
        boolean removeById = tarticleContentService.removeById(taricleContent.getId());
        ThrowUtils.throwIf(!removeById,ErrorCode.OPERATION_ERROR);
        // 删除文章分类
//        // select * from t_article_content where article_id = ?
//        QueryWrapper<TArticleContent> queryTArticleContent = new QueryWrapper<>();
//        queryTArticleContent.eq("article_id",id);
//        TArticleContent taricleContent = tarticleContentService.getOne(queryTArticleContent);
        boolean removeByArticleId = tarticleCategoryRelService.removeByArticleId(id);
        ThrowUtils.throwIf(!removeByArticleId,ErrorCode.OPERATION_ERROR);
        return ResultUtils.success(b);
    }

    /**
     * 更新
     *
     * @param tarticleUpdateRequest
     * @return
     */
    @PostMapping("/tarticle/update")
    public BaseResponse<Boolean> updateTArticle(@RequestBody TArticleUpdateRequest tarticleUpdateRequest, HttpServletRequest request) {
        if (tarticleUpdateRequest == null || tarticleUpdateRequest.getId() <= 0) {
            throw new BusinessException(ErrorCode.PARAMS_ERROR);
        }
        User user = userService.getLoginUser(request);
        TArticle tarticle = new TArticle();
        BeanUtils.copyProperties(tarticleUpdateRequest, tarticle);
        long id = tarticleUpdateRequest.getId();
        // 判断是否存在
        TArticle oldTArticle = tarticleService.getById(id);
        ThrowUtils.throwIf(oldTArticle == null, ErrorCode.NOT_FOUND_ERROR);
        // 仅本人或管理员可删除
        if (!oldTArticle.getUserId().equals(user.getId()) && !userService.isAdmin(request)) {
            throw new BusinessException(ErrorCode.NO_AUTH_ERROR);
        }
        boolean result = tarticleService.updateById(tarticle);
        // 更新文章内容
        //select * from t_article_content where article_id = ?
        QueryWrapper<TArticleContent> queryArticleContent = new QueryWrapper<>();
        queryArticleContent.eq("article_id",id);
        TArticleContent tarticleContent = tarticleContentService.getOne(queryArticleContent);
        tarticleContent.setContent(tarticleUpdateRequest.getContent());
        boolean updateById = tarticleContentService.updateById(tarticleContent);
        ThrowUtils.throwIf(!updateById,ErrorCode.OPERATION_ERROR);
        return ResultUtils.success(result);
    }

    /**
     * 分页获取文章列表（仅管理员可用）
     *
     * @param tArticleQueryRequest
     * @return
     */
    @PostMapping("/tarticle/list/page")
    @AuthCheck(mustRole = UserConstant.ADMIN_ROLE)
    public BaseResponse<Page<TArticle>> listTArticleByPage(@RequestBody TArticleQueryRequest tArticleQueryRequest) {
        long current = tArticleQueryRequest.getCurrent();
        long size = tArticleQueryRequest.getPageSize();
        // 查询数据库
        Page<TArticle> tArticlePage = tarticleService.page(new Page<>(current, size),
                getTArticleQueryWrapper(tArticleQueryRequest));
        return ResultUtils.success(tArticlePage);
    }

    @PostMapping("/tarticle/list")
    @AuthCheck(mustRole = UserConstant.ADMIN_ROLE)
    public BaseResponse<HashMap<String,Object>> listTArticle(@RequestBody TArticleQueryRequest tArticleQueryRequest) {
        List<ArticleVO> data = tarticleService.listTArticle(tArticleQueryRequest);
        Long count = tarticleService.getTArticleVOCount(tArticleQueryRequest);
        HashMap<String, Object> res = new HashMap<>();
        res.put("records",data);
        res.put("total",count);
        return ResultUtils.success(res);
    }

    /**
     * 根据 id 获取
     *
     * @param id
     * @return
     */
    @GetMapping("/tarticle/get/{id}")
    public BaseResponse<ArticleVO> getTArticleById(@PathVariable("id") long id, HttpServletRequest request) {
        if (id <= 0) {
            throw new BusinessException(ErrorCode.PARAMS_ERROR);
        }
        TArticle tarticle = tarticleService.getById(id);
        if (tarticle == null) {
            throw new BusinessException(ErrorCode.NOT_FOUND_ERROR);
        }
        ArticleVO articleVO = new ArticleVO();
        BeanUtils.copyProperties(tarticle, articleVO);
        //select *  from t_article_content where article_id = ?
        QueryWrapper<TArticleContent> queryArticleContent = new QueryWrapper<>();
        queryArticleContent.eq("article_id",articleVO.getId());
        TArticleContent tarticleContentServiceOne = tarticleContentService.getOne(queryArticleContent);
        articleVO.setContent(tarticleContentServiceOne.getContent());
        //所属分类
        //select *  from t_article_category_rel where article_id = ?
        QueryWrapper<TArticleCategoryRel> queryArticleCategoryRel = new QueryWrapper<>();
        queryArticleCategoryRel.eq("article_id",articleVO.getId());
        //List<TArticleCategoryRel> tArticleCategoryRelLists = tarticleCategoryRelService.list(queryArticleCategoryRel);
        TArticleCategoryRel tarticleCategoryRelServiceOne = tarticleCategoryRelService.getOne(queryArticleCategoryRel);
        //分类名称
        QueryWrapper<TCategory> queryCategory = new QueryWrapper<>();
        queryCategory.eq("id",tarticleCategoryRelServiceOne.getCategory_id());
        TCategory tcategoryServiceOne = tcategoryService.getOne(queryCategory);
        articleVO.setCategoryId(tcategoryServiceOne.getId());
        articleVO.setCategoryName(tcategoryServiceOne.getName());

        return ResultUtils.success(articleVO);
    }

    /**
     * 获取当前用户的文章列表
     * @param tarticleQueryRequest
     * @param request
     * @return
     */
    @PostMapping("/tarticle/my/list/page")
    public BaseResponse<Page<TArticle>> listMyPostVOByPage(@RequestBody TArticleQueryRequest tarticleQueryRequest,
                                                           HttpServletRequest request) {
        if (tarticleQueryRequest == null) {
            throw new BusinessException(ErrorCode.PARAMS_ERROR);
        }
        User loginUser = userService.getLoginUser(request);
        tarticleQueryRequest.setUserId(loginUser.getId());
        long current = tarticleQueryRequest.getCurrent();
        long size = tarticleQueryRequest.getPageSize();
        // 限制爬虫
        ThrowUtils.throwIf(size > 20, ErrorCode.PARAMS_ERROR);
        Page<TArticle> articleVOPage = tarticleService.page(new Page<>(current, size),
                getTArticleQueryWrapper(tarticleQueryRequest));
        return ResultUtils.success(articleVOPage);
    }

    private QueryWrapper<TArticle> getTArticleQueryWrapper(TArticleQueryRequest tarticleQueryRequest) {
        QueryWrapper<TArticle> queryWrapper = new QueryWrapper<>();
        if (tarticleQueryRequest == null){
            return queryWrapper;
        }

        Long userId = tarticleQueryRequest.getUserId();
        String title = tarticleQueryRequest.getTitle();
        String sortField = tarticleQueryRequest.getSortField();
        String sortOrder = tarticleQueryRequest.getSortOrder();

        queryWrapper.like(StringUtils.isNotBlank(title),"title",title);
        queryWrapper.eq(ObjectUtils.isNotEmpty(userId), "userId", userId);
        queryWrapper.eq("is_deleted", false);
        queryWrapper.orderBy(SqlUtils.validSortField(sortField), sortOrder.equals(CommonConstant.SORT_ORDER_ASC), sortField);

        return queryWrapper;
    }

    // endregion

}
