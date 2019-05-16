package com.lixingyong.meneusoft.modules.xcx.utils;

public enum Week {
    MON("一",1), TUE("二",2), WED("三",3), THU("四",4), FRI("五",5), SAT("六", 6), SUN("日", 7);
    private String weekName;
    private int weekIndex;

    private Week(String weekName, int weekIndex){
        this.weekName = weekName;
        this.weekIndex = weekIndex;
    }

    public static String getWeekName(int weekIndex) {
        for (Week week : Week.values()) {
            if (week.getWeekIndex() == weekIndex)
                return week.weekName;
        }
        return null;
    }


    public String getWeekName() {
        return weekName;
    }

    public void setWeekName(String weekName) {
        this.weekName = weekName;
    }

    public int getWeekIndex() {
        return weekIndex;
    }

    public void setWeekIndex(int weekIndex) {
        this.weekIndex = weekIndex;
    }
}
