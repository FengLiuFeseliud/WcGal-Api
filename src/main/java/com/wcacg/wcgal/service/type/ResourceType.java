package com.wcacg.wcgal.service.type;

public enum ResourceType {
    ARTICLE("ar"),
    COMMENT("co"),
    NULL("null");

    private final String name;

    ResourceType(String name){
        this.name = name;
    }

    public String getName() {
        return this.name;
    }

    public static ResourceType getType(String resourceId){
        String type = resourceId.split("_")[0];
        for (ResourceType value : ResourceType.values()) {
            if (value.name.equals(type)){
                return value;
            }
        }
        return NULL;
    }

    public static Long getId(String resourceId){
        String[] type = resourceId.split("_");
        if (type.length < 2){
            return 0L;
        }
        return Long.parseLong(type[1]);
    }
}