package com.yu.modules.dati.scoring;

import com.yu.modules.dati.model.entity.App;
import com.yu.modules.dati.model.entity.UserAnswer;

import java.util.List;

public interface ScoringStrategy {
    /**
     * 执行评分
     * @param choices
     * @param app
     * @return
     */
    UserAnswer doScore(List<String> choices, App app) throws Exception;
}
