package cn.geektang.privacyspace.common;

import cn.geektang.privacyspace.common.JsonConfig;

interface IPrivacyService {
    JsonConfig getConfig() = 1;
    boolean updateConfig(in JsonConfig config) = 2;
    int getFilterCount() = 3;
    String getLogs() = 4;
} 