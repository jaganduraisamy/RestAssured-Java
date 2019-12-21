public class SearchResult {

    public static String SEARCHVAL;
    public static String BLK_NO;
    public static String ROAD_NAME;
    public static String BUILDING;
    public static String ADDRESS;
    public static String POSTAL;

    public static boolean AreEqual(SearchResult result1, SearchResult result2) {

        return result1.SEARCHVAL.equals(result2.SEARCHVAL) && result1.BLK_NO.equals(result2.BLK_NO) && result1.ROAD_NAME.equals(result2.ROAD_NAME)
                && result1.BUILDING.equals(result2.BUILDING) && result1.ADDRESS.equals(result2.ADDRESS) && result1.POSTAL.equals(result2.POSTAL);
    }
}
