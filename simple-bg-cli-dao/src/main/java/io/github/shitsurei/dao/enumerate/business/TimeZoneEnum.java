package io.github.shitsurei.dao.enumerate.business;

/**
 * @author zgr
 * @Description 时区枚举（按时区由西到东排序）
 * @createTime 2022年02月27日 15:17:00
 */
public enum TimeZoneEnum {
    AST("America/Anchorage", "GMT-9", "Alaska,America Anchorage"),
    PST("America/Los_Angeles", "GMT-8", "Pacific Time(US & Canada),Tijuana,America Los_Angeles"),
    PNT("America/Phoenix", "GMT-7", "Arizona,Chihuahua,Mazatlan,Mountain Time(US & Canada),America Phoenix"),
    CST("America/Chicago", "GMT-6", "Central America,Central Time(US & Canada),Guadalajara,Mexico City,Monterrey,Saskatchewan,America Chicago"),
    IET("America/Indiana/Indianapolis", "GMT-5", "Bogota,Eastern Time(US & Canada),Indiana(East),Lima,Quito"),
    PRT("America/Puerto_Rico", "GMT-4", "Atlantic Time(Canada),Caracas,Georgetown,La Paz,Puerto Rico,Santiago,America Puerto_Rico,St_Johns"),
    // CNT("America/St_Johns", "GMT-4",""),
    AGT("America/Argentina/Buenos_Aires", "GMT-3", "Brasilia,Buenos Aires,Greenland,Montevideo,Sao_Paulo,Argentina"),
    // BET("America/Sao_Paulo", "GMT-3",""),
    DIY1("GMT-2", "GMT-2", "Mid-Atlantic"),
    DIY2("GMT-1", "GMT-1", "Azores,Cape Verde Is"),
    EST("-05:00", "GMT+0", "UTC,Edinburgh,Lisbon,London,Monrovia"),
    // MST("-07:00", "GMT+0",""),
    // HST("-10:00", "GMT+0",""),
    ECT("Europe/Paris", "GMT+1", "Amsterdam,Belgrade,Berlin,Bern,Bratislava,Brussels,Budapest,Casablanca,Copenhagen,Dublin,Ljubljana,Madrid,Paris,Prague,Rome,Sarajevo,Skopje,Stockholm,Vienna,Warsaw,West Central Africa,Zagreb,Zurich"),
    ART("Africa/Cairo", "GMT+2", "Athens,Bucharest,Cairo,Harare,Helsinki,Jerusalem,Kaliningrad,Kyiv,Pretoria,Riga,Sofia,Tallinn,Vilnius"),
    // CAT("Africa/Harare", "GMT+2",""),
    EAT("Africa/Addis_Ababa", "GMT+3", "Baghdad,Istanbul,Kuwait,Minsk,Moscow,Nairobi,Riyadh,St. Petersburg"),
    NET("Asia/Yerevan", "GMT+4", "Abu Dhabi,Baku,Muscat,Samara,Tbilisi,Volgograd,Yerevan"),
    PLT("Asia/Karachi", "GMT+5", "Ekaterinburg,Islamabad,Karachi,Kolkata,Tashkent"),
    // IST("Asia/Kolkata", "GMT+5",""),
    BST("Asia/Dhaka", "GMT+6", "Almaty,Astana,Dhaka,Urumqi"),
    VST("Asia/Ho_Chi_Minh", "GMT+7", "Bangkok,Hanoi,Jakarta,Krasnoyarsk,Novosibirsk"),
    CTT("Asia/Shanghai", "GMT+8", "Beijing,Chongqing,Hong Kong,Irkutsk,Kuala Lumpur,Perth,Singapore,Taipei,Ulaanbaatar,Shanghai"),
    JST("Asia/Tokyo", "GMT+9", "Osaka,Sapporo,Seoul,Tokyo,Yakutsk,Darwin"),
    // ACT("Australia/Darwin", "GMT+9",""),
    DIY3("GMT+10", "GMT+10", "Brisbane,Canberra,Guam,Hobart,Melbourne,Port Moresby,Sydney,Vladivostok"),
    AET("Australia/Sydney", "GMT+11", "Magadan,New Caledonia,Solomon Is.,Srednekolymsk,Australia Sydney,Pacific Guadalcanal"),
    // SST("Pacific/Guadalcanal", "GMT+11",""),
    DIY4("GMT+12", "GMT+12", "Auckland,Fiji,Kamchatka,Marshall Is.,Wellington"),
    NST("Pacific/Auckland", "GMT-11", "American Samoa,Midway Island,Pacific Auckland,Nuku'alofa,Samoa,Tokelau Is."),
    MIT("Pacific/Apia", "GMT-10", "Hawaii,Pacific Apia"),
    ;
    private String id;
    private String offset;
    private String mainCity;

    TimeZoneEnum(String id, String offset, String mainCity) {
        this.id = id;
        this.offset = offset;
        this.mainCity = mainCity;
    }

    public String getId() {
        return id;
    }

    public String getOffset() {
        return offset;
    }

    public String getMainCity() {
        return mainCity;
    }
}


