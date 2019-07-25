package Util;

public class Util {

    public static String getFieldMapping(FieldEnum fieldEnum){

        switch (fieldEnum){
            case FIELD_CONTENTS:
                return Config.FIELD_CONTENTS;

            case FIELD_MODIFIED_DATE:
                return Config.FIELD_MODIFIED_DATE;

            case FIELD_PATH:
                return Config.FIELD_PATH;
        }
        return null;
    }
}
