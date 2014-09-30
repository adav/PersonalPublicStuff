package Model.RecommendationModel.CustomTypes.POJOs.User.RecommendationPreferences;

import com.mongodb.BasicDBObject;

import java.util.Calendar;
import java.util.Date;

/**
 * User: adav
 */
public class WithinSiteUserRecAutoCheckinPrefs extends BasicDBObject{

    enum TimePeriod{
        MORNING,   //7->11
        MIDDAY,    //12->14
        AFTERNOON, //15->17
        EVENING,   //18->2
        NIGHT      //23->6
    }

    public WithinSiteUserRecAutoCheckinPrefs(){}

    public WithinSiteUserRecAutoCheckinPrefs(BasicDBObject basic){
        super(basic.toMap());
    }

    public static TimePeriod getTimePeriodEnum(Date date){
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(date);
        int hour = calendar.get(Calendar.HOUR_OF_DAY);

        TimePeriod periodKey;

        if(hour < 7){
            periodKey = TimePeriod.NIGHT;
        } else if (hour < 12){
            periodKey = TimePeriod.MORNING;
        } else if (hour < 15){
            periodKey = TimePeriod.MIDDAY;
        } else if (hour < 18){
            periodKey = TimePeriod.AFTERNOON;
        } else if (hour < 23){
            periodKey = TimePeriod.EVENING;
        } else {
            periodKey = TimePeriod.NIGHT;
        }

        return periodKey;
    }

    public static String getTimePeriod(Date date){
        return getTimePeriodEnum(date).name();
    }

    public BasicDBObject getPrefsForPeriod(TimePeriod period){
        return (BasicDBObject) get(period.name());
    }

    public static String sanitiseCategoryForPreferences(String category){
        return category.toUpperCase().replaceAll("[^A-Z0-9]", "");
    }

    public int getFrequencyForPeriodAndUnsanitisedCategory(TimePeriod period, String category){
        BasicDBObject prefsForPeriod = getPrefsForPeriod(period);
        String sanitisedCat = sanitiseCategoryForPreferences(category);

        try{
            return getInt(sanitisedCat);
        } catch (Exception e){
            return 0;
        }
    }
}
