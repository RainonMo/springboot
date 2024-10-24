package com.yu.job.read;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.yu.model.entity.Message;
import com.yu.model.entity.UserTimeSet;
import com.yu.service.MessageService;
import com.yu.service.UserTimeSetService;
import org.springframework.scheduling.annotation.SchedulingConfigurer;
import org.springframework.scheduling.concurrent.ThreadPoolTaskScheduler;
import org.springframework.scheduling.config.ScheduledTaskRegistrar;
import org.springframework.scheduling.config.TriggerTask;
import org.springframework.scheduling.support.CronTrigger;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Component
public class ReadTask implements SchedulingConfigurer {
    //间隔1s查询定时任务
//    @Scheduled(fixedRate = 1000)
//    private void myTasks() {
//        System.out.println("我是一个定时任务");
//        //执行用户的定时任务
//
//    }
    @Resource
    private UserTimeSetService userTimeSetService;
    @Resource
    private MessageService messageService;

    private ScheduledTaskRegistrar taskRegistrar;

    @Override
    public void configureTasks(ScheduledTaskRegistrar scheduledTaskRegistrar) {
        List<Message> listMessage = messageService.list();
        List<TriggerTask> list= new ArrayList<>();
        //循环添加任务
        listMessage.forEach(t->{
            TriggerTask triggerTask = new TriggerTask(()->{
                System.out.println("执行定时任务："+ LocalDateTime.now().toLocalTime());
            },triggerContext -> {
                System.out.println("执行message:"+t.getTimeId()+"消息："+t.getContent());
                QueryWrapper<UserTimeSet> wrapperTCategory = new QueryWrapper<>();
                wrapperTCategory.eq("id",t.getTimeId());
                UserTimeSet one = userTimeSetService.getOne(wrapperTCategory);
                //如果需要动态的指定当前定时任务的执行corn。这里可以增加一步，查询数据库操作。如果任务corn不需要精确修改，corn可进行缓存。到期在去查询数据库。这里根据读者的需求自行取舍。
                return new CronTrigger(one.getCron()).nextExecutionTime(triggerContext);
            });
            list.add(triggerTask);
        });
        //将任务列表注册到定时器
        scheduledTaskRegistrar.setTriggerTasksList(list);
        this.taskRegistrar = scheduledTaskRegistrar;
    }

    private void process() {
        //select * from message where userId = ?
        QueryWrapper<Message> wrapperTCategory = new QueryWrapper<>();
        wrapperTCategory.eq("userId","1789213389879713793");
        Message message = messageService.getOne(wrapperTCategory);
        System.out.println("基于接口定时任务："+message.getContent());

    }

}
