package Simpmc.Rewards.util;

public class TimeUtil {

    public static String formatMinutes(long minutes) {

        long hours = minutes / 60;
        long mins = minutes % 60;

        return hours + "小时 " + mins + "分钟";
    }
}
