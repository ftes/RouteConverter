/*
    This file is part of RouteConverter.

    RouteConverter is free software; you can redistribute it and/or modify
    it under the terms of the GNU General Public License as published by
    the Free Software Foundation; either version 2 of the License, or
    (at your option) any later version.

    RouteConverter is distributed in the hope that it will be useful,
    but WITHOUT ANY WARRANTY; without even the implied warranty of
    MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
    GNU General Public License for more details.

    You should have received a copy of the GNU General Public License
    along with RouteConverter; if not, write to the Free Software
    Foundation, Inc., 51 Franklin St, Fifth Floor, Boston, MA  02110-1301  USA

    Copyright (C) 2007 Christian Pesch. All Rights Reserved.
*/

package slash.navigation.util;

import slash.navigation.BaseNavigationFormat;
import slash.navigation.BaseNavigationPosition;
import slash.navigation.BaseRoute;
import slash.navigation.Wgs84Position;
import slash.navigation.itn.TomTomPosition;
import slash.common.io.Transfer;
import slash.common.io.CompactCalendar;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.*;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Helpers for processing of comments of positions
 *
 * @author Christian Pesch
 */
public abstract class RouteComments {
    private static final int MAXIMUM_ROUTE_NAME_LENGTH = 50;

    private static final String POSITION = "Position";
    private static final Pattern POSITION_PATTERN = Pattern.compile("(.*)" + POSITION + " (\\d+)(.*)");
    private static final Pattern NUMBER_PATTERN = Pattern.compile("(\\d*)(.*)");

    public static String shortenRouteName(BaseRoute route) {
        String result = "?";
        if (route != null) {
            if (route.getName() != null)
                result = route.getName();
            if (result.length() > MAXIMUM_ROUTE_NAME_LENGTH)
                result = result.substring(0, MAXIMUM_ROUTE_NAME_LENGTH) + "...";
        }
        return result;
    }

    public static String createRouteName(List<? extends BaseNavigationPosition> positions) {
        if (positions.size() > 0)
            return positions.get(0).getComment() + " to " + positions.get(positions.size() - 1).getComment();
        else
            return "?";
    }

    public static void commentRouteName(BaseRoute<BaseNavigationPosition, BaseNavigationFormat> route) {
        if (route.getName() == null) {
            route.setName(createRouteName(route.getPositions()));
        }
    }

    public static String createRouteDescription(BaseRoute route) {
        String name = Transfer.trim(route.getName());
        List<String> description = route.getDescription();
        StringBuffer buffer = new StringBuffer();
        if (name != null)
            buffer.append(name);
        if (description != null) {
            for (String line : description)
                buffer.append(line);
        }
        return buffer.toString();
    }


    private static String getPositionComment(int index) {
        return POSITION + " " + (index + 1);
    }

    public static boolean isPositionComment(String comment) {
        Matcher matcher = POSITION_PATTERN.matcher(comment);
        return matcher.matches();
    }

    public static void commentPositions(List<? extends BaseNavigationPosition> positions) {
        for (int i = 0; i < positions.size(); i++) {
            BaseNavigationPosition position = positions.get(i);
            commentPosition(position, i);
        }
    }

    private static void commentPosition(BaseNavigationPosition position, int index) {
        if (position.getComment() == null || "(null)".equals(position.getComment()))
            position.setComment(getPositionComment(index));
        else {
            Matcher matcher = POSITION_PATTERN.matcher(position.getComment());
            if (matcher.matches()) {
                String prefix = matcher.group(1);
                String postfix = matcher.group(3);
                position.setComment(prefix + getPositionComment(index) + postfix);
            }
        }
    }

    public static void numberPosition(BaseNavigationPosition position, int index,
                                      int digitCount, boolean spaceBetweenNumberAndComment) {
        commentPosition(position, index);
        Matcher matcher = NUMBER_PATTERN.matcher(position.getComment());
        if (matcher.matches()) {
            String postfix = Transfer.trim(matcher.group(2));
            String prefix = Transfer.formatIntAsString((index + 1), digitCount);
            position.setComment(prefix + (spaceBetweenNumberAndComment ? " " : "") + postfix);
        }
    }

    public static void commentRoutePositions(List<? extends BaseRoute> routes) {
        Map<LongitudeAndLatitude, String> comments = new HashMap<LongitudeAndLatitude, String>();

        for (BaseRoute<BaseNavigationPosition, BaseNavigationFormat> route : routes) {
            for (BaseNavigationPosition position : route.getPositions()) {
                if (position.getComment() == null || !position.hasCoordinates())
                    continue;

                LongitudeAndLatitude lal = new LongitudeAndLatitude(position);
                if (comments.get(lal) == null) {
                    comments.put(lal, position.getComment());
                }
            }
        }

        for (BaseRoute<BaseNavigationPosition, BaseNavigationFormat> route : routes) {
            for (BaseNavigationPosition position : route.getPositions()) {
                if (position.getComment() == null && position.hasCoordinates()) {
                    LongitudeAndLatitude lal = new LongitudeAndLatitude(position);
                    String comment = comments.get(lal);
                    if (comment != null) {
                        position.setComment(comment);
                    }
                }
            }
        }

        for (BaseRoute<BaseNavigationPosition, BaseNavigationFormat> route : routes) {
            commentPositions(route.getPositions());
        }
    }


    private static class LongitudeAndLatitude {
        public final double longitude, latitude;

        public LongitudeAndLatitude(BaseNavigationPosition position) {
            this.longitude = position.getLongitude();
            this.latitude = position.getLatitude();
        }

        public boolean equals(Object o) {
            if (this == o) return true;
            if (o == null || getClass() != o.getClass()) return false;

            final LongitudeAndLatitude that = (LongitudeAndLatitude) o;

            return Double.compare(that.latitude, latitude) == 0 &&
                    Double.compare(that.longitude, longitude) == 0;
        }

        public int hashCode() {
            int result;
            long temp;
            temp = longitude != +0.0d ? Double.doubleToLongBits(longitude) : 0L;
            result = (int) (temp ^ (temp >>> 32));
            temp = latitude != +0.0d ? Double.doubleToLongBits(latitude) : 0L;
            result = 29 * result + (int) (temp ^ (temp >>> 32));
            return result;
        }
    }


    public static final SimpleDateFormat TRIPMASTER_TIME = new SimpleDateFormat("HH:mm:ss");
    public static final SimpleDateFormat TRIPMASTER_DATE = new SimpleDateFormat("dd/MM/yyyy HH:mm:ss");

    private static final String TIME = "\\d{1,2}:\\d{2}:\\d{2}";
    private static final String DATE = "\\d{6}";
    private static final String DOUBLE = "[-\\d\\.]+";
    private static final String TRIPMASTER_REASONS = "Dur. " + TIME + "|Dauer " + TIME + "|" +
            "Abstand \\d+|Dist. \\d+|Distanz \\d+|Km " + DOUBLE + "|" +
            "Course \\d+|Cape \\d+|Kurs \\d+|Richtung \\d+|" +
            "Waypoint|Wpt|Punkt|Pause";

    private static final Pattern TRIPMASTER_HEADING_PATTERN = Pattern.compile("(Course|Cape|Kurs|Richtung) (\\d+)");
    private static final Pattern TRIPMASTER_1dot4_PATTERN = Pattern.compile("(" + TRIPMASTER_REASONS + ") - (" + TIME + ") - (" + DOUBLE + ") m - (.+)");
    private static final Pattern TRIPMASTER_SHORT_STARTEND_PATTERN = Pattern.compile(
            "(Start|Ende|Finish) : ((.+) - )?(.+) - (.+) - (" + DOUBLE + ") m - (" + DOUBLE + ") (K|k)m");
    private static final Pattern TRIPMASTER_SHORT_WAYPOINT_PATTERN = Pattern.compile("(" + TIME + ") - (" + DOUBLE + ") m");
    private static final Pattern TRIPMASTER_MIDDLE_PATTERN = Pattern.compile(
            "(" + TIME + ") - (" + TRIPMASTER_REASONS + ")(\\s?:\\s.+)? - (" + DOUBLE + ") m - (" + DOUBLE + ") (K|k)m");
    private static final Pattern TRIPMASTER_LONG_NO_REASON_PATTERN = Pattern.compile(
            "(" + TIME + ") - (.+) - (" + DOUBLE + ") m - (" + DOUBLE + ") (K|k)m - (" + DOUBLE + ") (K|k)m/h( - \\d+)?");
    private static final Pattern TRIPMASTER_LONG_PATTERN = Pattern.compile(
            "(" + TIME + ") - (Start : (.*?)|Finish : (.*?)|" + TRIPMASTER_REASONS + ")(\\s?:\\s.+)? - " +
            "(" + DOUBLE + ") m - (" + DOUBLE + ") (K|k)m - (" + DOUBLE + ") (K|k)m/h( - \\d+)?");

    /**
     * logpos encoding of the comment:
     * + looks like a planned position with a verbose comment
     * + Rottstuecker (Wiesloch); K4174 Horrenberger Strasse @166.6m (s=60 d=34)
     * - looks like a tracked position with a verbose comment
     * - Rottstuecker (Wiesloch); K4174 Horrenberger Strasse @162.6m (s=66 d=6)
     * * is a coordinate comment
     * * 1000462:4889518 @365.8m (s=1 d=193)
     * = seems to be written if the position does not change for a time period
     * = 1000466:4889529 (@365.8m 090314 07:36:52 - 090314 08:02:04)
     */
    private static final String COMMENT_SEPARATOR = "(\\+|-|\\*|=)";
    private static final SimpleDateFormat LOGPOS_DATE = new SimpleDateFormat("yyMMdd HH:mm:ss");
    private static final Pattern LOGPOS_1_PATTERN = Pattern.compile("(" + DATE + " " + TIME + "): " +
            COMMENT_SEPARATOR + " (.+) \\(?@(" + DOUBLE + "|\\?)m \\(?((s=(\\d+) d=(\\d+))?.*)\\)");
    private static final Pattern LOGPOS_2_PATTERN = Pattern.compile("(" + DATE + " " + TIME + "): " +
            COMMENT_SEPARATOR + " (.+) \\((s=(\\d+) d=(\\d+))\\)");


    private static final String TTTRACKLOG_NUMBER = "\\d+\\.?\\d?";
    private static final String TTTRACKLOG_REASONS = "Start|End|" +
            TTTRACKLOG_NUMBER + " \\w+ Pause( (" + TTTRACKLOG_NUMBER + ")m)?|" +
            "v=(" + TTTRACKLOG_NUMBER + ") alt=(" + TTTRACKLOG_NUMBER + ")";
    private static final Pattern TTTRACKLOG_PATTERN = Pattern.compile("(\\d{2}:\\d{2}:?\\d{0,2}) " +
            "(" + TTTRACKLOG_REASONS + ") .*");


    private static CompactCalendar parse(String string, DateFormat dateFormat) {
        if (string == null)
            return null;
        try {
            Date date = dateFormat.parse(string);
            Calendar time = Calendar.getInstance();
            time.setTime(date);
            return CompactCalendar.fromCalendar(time);
        } catch (ParseException e) {
            return null;
        }
    }

    private static CompactCalendar parseTripmaster1dot4Time(String string) {
        return parse(string, TRIPMASTER_TIME);
    }

    private static CompactCalendar parseTripmaster1dot8Date(String string) {
        return parse(string, TRIPMASTER_DATE);
    }

    public static Double parseTripmasterHeading(String string) {
        Matcher matcher = TRIPMASTER_HEADING_PATTERN.matcher(string);
        if (matcher.matches()) {
            return Transfer.parseDouble(matcher.group(2));
        }
        return null;
    }

    private static CompactCalendar parseLogposDate(String string) {
        return parse(string, LOGPOS_DATE);
    }

    private static CompactCalendar parseTTTracklogTime(String string) {
        if (string.length() == 5)
            string += ":00";
        return parseTripmaster1dot4Time(string);
    }


    public static void parseComment(BaseNavigationPosition position, String comment) {
        Matcher matcher = TRIPMASTER_1dot4_PATTERN.matcher(comment);
        if (matcher.matches()) {
            position.setTime(parseTripmaster1dot4Time(matcher.group(2)));
            position.setElevation(Transfer.parseDouble(matcher.group(3)));

            if (position instanceof TomTomPosition) {
                TomTomPosition tomTomPosition = (TomTomPosition) position;
                String reason = Transfer.trim(matcher.group(1));
                tomTomPosition.setReason(reason);
                tomTomPosition.setHeading(parseTripmasterHeading(reason));
                tomTomPosition.setCity(Transfer.trim(matcher.group(4)));
            }

            if  (position instanceof Wgs84Position) {
                Wgs84Position wgs84Position = (Wgs84Position) position;
                String reason = Transfer.trim(matcher.group(1));
                wgs84Position.setHeading(parseTripmasterHeading(reason));
            }
        }

        matcher = TRIPMASTER_SHORT_STARTEND_PATTERN.matcher(comment);
        if (matcher.matches()) {
            String dateStr = Transfer.trim(matcher.group(4));
            String timeStr = Transfer.trim(matcher.group(5));
            position.setTime(parseTripmaster1dot8Date(dateStr + " " + timeStr));
            if (position.getTime() == null)
                position.setTime(parseTripmaster1dot4Time(timeStr));
            position.setElevation(Transfer.parseDouble(matcher.group(6)));

            if (position instanceof TomTomPosition) {
                TomTomPosition tomTomPosition = (TomTomPosition) position;
                String city = Transfer.trim(matcher.group(3));
                if (city == null) {
                    city = dateStr;
                    dateStr = null;
                }
                tomTomPosition.setReason(Transfer.trim(matcher.group(1)) + " : " + (dateStr != null ? dateStr + " - " : "") + timeStr);
                tomTomPosition.setCity(city);
            }
        }

        matcher = TRIPMASTER_SHORT_WAYPOINT_PATTERN.matcher(comment);
        if (matcher.matches()) {
            position.setTime(parseTripmaster1dot4Time(matcher.group(1)));
            position.setElevation(Transfer.parseDouble(matcher.group(2)));

            if (position instanceof TomTomPosition) {
                TomTomPosition tomTomPosition = (TomTomPosition) position;
                tomTomPosition.setReason("Waypoint");
                tomTomPosition.setCity(null);
            }

        }

        matcher = TRIPMASTER_MIDDLE_PATTERN.matcher(comment);
        if (matcher.matches()) {
            position.setTime(parseTripmaster1dot4Time(Transfer.trim(matcher.group(1))));
            position.setElevation(Transfer.parseDouble(matcher.group(4)));

            if (position instanceof TomTomPosition) {
                TomTomPosition tomTomPosition = (TomTomPosition) position;
                String city = Transfer.trim(matcher.group(3));
                if (city != null && city.startsWith(": "))
                    city = Transfer.trim(city.substring(2, city.length()));
                String reason = Transfer.trim(matcher.group(2));
                if (reason == null)
                    reason = city;
                tomTomPosition.setCity(city);
                tomTomPosition.setReason(reason);
            }
        }

        matcher = TRIPMASTER_LONG_NO_REASON_PATTERN.matcher(comment);
        if (matcher.matches()) {
            position.setTime(parseTripmaster1dot4Time(Transfer.trim(matcher.group(1))));
            position.setSpeed(Transfer.parseDouble(matcher.group(6)));
            position.setElevation(Transfer.parseDouble(matcher.group(3)));

            if (position instanceof TomTomPosition) {
                TomTomPosition tomTomPosition = (TomTomPosition) position;
                String city = Transfer.trim(matcher.group(2));
                if (city != null && city.startsWith(": "))
                    city = Transfer.trim(city.substring(2, city.length()));
                tomTomPosition.setCity(city);
                tomTomPosition.setReason(city);
            }
        }

        matcher = TRIPMASTER_LONG_PATTERN.matcher(comment);
        if (matcher.matches()) {
            position.setTime(parseTripmaster1dot8Date(matcher.group(3)));
            if (position.getTime() == null)
                position.setTime(parseTripmaster1dot4Time(matcher.group(1)));
            position.setSpeed(Transfer.parseDouble(matcher.group(9)));
            position.setElevation(Transfer.parseDouble(matcher.group(6)));

            if (position instanceof TomTomPosition) {
                TomTomPosition tomTomPosition = (TomTomPosition) position;
                String city = Transfer.trim(matcher.group(5));
                if (city != null && city.startsWith(": "))
                    city = Transfer.trim(city.substring(2, city.length()));
                String reason = Transfer.trim(matcher.group(2));
                if (city == null)
                    city = reason;
                tomTomPosition.setCity(city);
                tomTomPosition.setReason(reason);
            }
        }

        matcher = LOGPOS_2_PATTERN.matcher(comment);
        if (matcher.matches()) {
            position.setTime(parseLogposDate(matcher.group(1)));
            position.setSpeed(Transfer.parseDouble(matcher.group(5)));

            if (position instanceof TomTomPosition) {
                TomTomPosition tomTomPosition = (TomTomPosition) position;
                tomTomPosition.setReason(Transfer.trim(matcher.group(4)));
                tomTomPosition.setCity(Transfer.trim(matcher.group(3)));
                tomTomPosition.setHeading(Transfer.parseDouble(matcher.group(6)));
            }
        }

        matcher = LOGPOS_1_PATTERN.matcher(comment);
        if (matcher.matches()) {
            position.setTime(parseLogposDate(matcher.group(1)));
            Double elevation;
            try {
                elevation = Transfer.parseDouble(matcher.group(4));
            } catch (NumberFormatException e) {
                elevation = null;
            }
            position.setElevation(elevation);
            position.setSpeed(Transfer.parseDouble(matcher.group(7)));

            if (position instanceof TomTomPosition) {
                TomTomPosition tomTomPosition = (TomTomPosition) position;
                tomTomPosition.setReason(Transfer.trim(matcher.group(5)));
                tomTomPosition.setCity(Transfer.trim(matcher.group(3)));
                tomTomPosition.setHeading(Transfer.parseDouble(matcher.group(8)));
            }
        }

        matcher = TTTRACKLOG_PATTERN.matcher(comment);
        if (matcher.matches()) {
            position.setTime(parseTTTracklogTime(matcher.group(1)));
            position.setSpeed(Transfer.parseDouble(matcher.group(5)));
            Double elevation = Transfer.parseDouble(matcher.group(6));
            if(elevation == null)
                elevation = Transfer.parseDouble(matcher.group(4)); // pause with elevation
            position.setElevation(elevation);
            if (position instanceof TomTomPosition) {
                TomTomPosition tomTomPosition = (TomTomPosition) position;
                tomTomPosition.setReason(Transfer.trim(matcher.group(2)));
            }
        }
    }
}