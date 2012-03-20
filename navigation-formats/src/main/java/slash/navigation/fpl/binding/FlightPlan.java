//
// This file was generated by the JavaTM Architecture for XML Binding(JAXB) Reference Implementation, vhudson-jaxb-ri-2.1-646 
// See <a href="http://java.sun.com/xml/jaxb">http://java.sun.com/xml/jaxb</a> 
// Any modifications to this file will be lost upon recompilation of the source schema. 
// Generated on: 2012.03.20 at 03:41:45 PM CET 
//


package slash.navigation.fpl.binding;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;
import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlSchemaType;
import javax.xml.bind.annotation.XmlType;
import javax.xml.datatype.XMLGregorianCalendar;


/**
 * <p>Java class for anonymous complex type.
 * 
 * <p>The following schema fragment specifies the expected content contained within this class.
 * 
 * <pre>
 * &lt;complexType>
 *   &lt;complexContent>
 *     &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
 *       &lt;sequence>
 *         &lt;element name="created" type="{http://www.w3.org/2001/XMLSchema}dateTime"/>
 *         &lt;element name="waypoint-table">
 *           &lt;complexType>
 *             &lt;complexContent>
 *               &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
 *                 &lt;sequence>
 *                   &lt;element name="waypoint" maxOccurs="unbounded">
 *                     &lt;complexType>
 *                       &lt;complexContent>
 *                         &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
 *                           &lt;sequence>
 *                             &lt;element name="identifier" type="{http://www.w3.org/2001/XMLSchema}string"/>
 *                             &lt;element name="type" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
 *                             &lt;element name="country-code" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
 *                             &lt;element name="lat" type="{http://www.w3.org/2001/XMLSchema}decimal"/>
 *                             &lt;element name="lon" type="{http://www.w3.org/2001/XMLSchema}decimal"/>
 *                             &lt;element name="comment" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
 *                             &lt;element name="elevation" type="{http://www.w3.org/2001/XMLSchema}decimal" minOccurs="0"/>
 *                           &lt;/sequence>
 *                         &lt;/restriction>
 *                       &lt;/complexContent>
 *                     &lt;/complexType>
 *                   &lt;/element>
 *                 &lt;/sequence>
 *               &lt;/restriction>
 *             &lt;/complexContent>
 *           &lt;/complexType>
 *         &lt;/element>
 *         &lt;element name="route">
 *           &lt;complexType>
 *             &lt;complexContent>
 *               &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
 *                 &lt;sequence>
 *                   &lt;element name="route-name" type="{http://www.w3.org/2001/XMLSchema}string"/>
 *                   &lt;element name="route-description" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
 *                   &lt;element name="flight-plan-index" type="{http://www.w3.org/2001/XMLSchema}unsignedByte"/>
 *                   &lt;element name="route-point" maxOccurs="unbounded">
 *                     &lt;complexType>
 *                       &lt;complexContent>
 *                         &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
 *                           &lt;sequence>
 *                             &lt;element name="waypoint-identifier" type="{http://www.w3.org/2001/XMLSchema}string"/>
 *                             &lt;element name="waypoint-type" type="{http://www.w3.org/2001/XMLSchema}string"/>
 *                             &lt;element name="waypoint-country-code" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
 *                           &lt;/sequence>
 *                         &lt;/restriction>
 *                       &lt;/complexContent>
 *                     &lt;/complexType>
 *                   &lt;/element>
 *                 &lt;/sequence>
 *               &lt;/restriction>
 *             &lt;/complexContent>
 *           &lt;/complexType>
 *         &lt;/element>
 *       &lt;/sequence>
 *     &lt;/restriction>
 *   &lt;/complexContent>
 * &lt;/complexType>
 * </pre>
 * 
 * 
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "", propOrder = {
    "created",
    "waypointTable",
    "route"
})
@XmlRootElement(name = "flight-plan")
public class FlightPlan {

    @XmlElement(required = true)
    @XmlSchemaType(name = "dateTime")
    protected XMLGregorianCalendar created;
    @XmlElement(name = "waypoint-table", required = true)
    protected FlightPlan.WaypointTable waypointTable;
    @XmlElement(required = true)
    protected FlightPlan.Route route;

    /**
     * Gets the value of the created property.
     * 
     * @return
     *     possible object is
     *     {@link XMLGregorianCalendar }
     *     
     */
    public XMLGregorianCalendar getCreated() {
        return created;
    }

    /**
     * Sets the value of the created property.
     * 
     * @param value
     *     allowed object is
     *     {@link XMLGregorianCalendar }
     *     
     */
    public void setCreated(XMLGregorianCalendar value) {
        this.created = value;
    }

    /**
     * Gets the value of the waypointTable property.
     * 
     * @return
     *     possible object is
     *     {@link FlightPlan.WaypointTable }
     *     
     */
    public FlightPlan.WaypointTable getWaypointTable() {
        return waypointTable;
    }

    /**
     * Sets the value of the waypointTable property.
     * 
     * @param value
     *     allowed object is
     *     {@link FlightPlan.WaypointTable }
     *     
     */
    public void setWaypointTable(FlightPlan.WaypointTable value) {
        this.waypointTable = value;
    }

    /**
     * Gets the value of the route property.
     * 
     * @return
     *     possible object is
     *     {@link FlightPlan.Route }
     *     
     */
    public FlightPlan.Route getRoute() {
        return route;
    }

    /**
     * Sets the value of the route property.
     * 
     * @param value
     *     allowed object is
     *     {@link FlightPlan.Route }
     *     
     */
    public void setRoute(FlightPlan.Route value) {
        this.route = value;
    }


    /**
     * <p>Java class for anonymous complex type.
     * 
     * <p>The following schema fragment specifies the expected content contained within this class.
     * 
     * <pre>
     * &lt;complexType>
     *   &lt;complexContent>
     *     &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
     *       &lt;sequence>
     *         &lt;element name="route-name" type="{http://www.w3.org/2001/XMLSchema}string"/>
     *         &lt;element name="route-description" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
     *         &lt;element name="flight-plan-index" type="{http://www.w3.org/2001/XMLSchema}unsignedByte"/>
     *         &lt;element name="route-point" maxOccurs="unbounded">
     *           &lt;complexType>
     *             &lt;complexContent>
     *               &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
     *                 &lt;sequence>
     *                   &lt;element name="waypoint-identifier" type="{http://www.w3.org/2001/XMLSchema}string"/>
     *                   &lt;element name="waypoint-type" type="{http://www.w3.org/2001/XMLSchema}string"/>
     *                   &lt;element name="waypoint-country-code" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
     *                 &lt;/sequence>
     *               &lt;/restriction>
     *             &lt;/complexContent>
     *           &lt;/complexType>
     *         &lt;/element>
     *       &lt;/sequence>
     *     &lt;/restriction>
     *   &lt;/complexContent>
     * &lt;/complexType>
     * </pre>
     * 
     * 
     */
    @XmlAccessorType(XmlAccessType.FIELD)
    @XmlType(name = "", propOrder = {
        "routeName",
        "routeDescription",
        "flightPlanIndex",
        "routePoint"
    })
    public static class Route {

        @XmlElement(name = "route-name", required = true)
        protected String routeName;
        @XmlElement(name = "route-description")
        protected String routeDescription;
        @XmlElement(name = "flight-plan-index")
        @XmlSchemaType(name = "unsignedByte")
        protected short flightPlanIndex;
        @XmlElement(name = "route-point", required = true)
        protected List<FlightPlan.Route.RoutePoint> routePoint;

        /**
         * Gets the value of the routeName property.
         * 
         * @return
         *     possible object is
         *     {@link String }
         *     
         */
        public String getRouteName() {
            return routeName;
        }

        /**
         * Sets the value of the routeName property.
         * 
         * @param value
         *     allowed object is
         *     {@link String }
         *     
         */
        public void setRouteName(String value) {
            this.routeName = value;
        }

        /**
         * Gets the value of the routeDescription property.
         * 
         * @return
         *     possible object is
         *     {@link String }
         *     
         */
        public String getRouteDescription() {
            return routeDescription;
        }

        /**
         * Sets the value of the routeDescription property.
         * 
         * @param value
         *     allowed object is
         *     {@link String }
         *     
         */
        public void setRouteDescription(String value) {
            this.routeDescription = value;
        }

        /**
         * Gets the value of the flightPlanIndex property.
         * 
         */
        public short getFlightPlanIndex() {
            return flightPlanIndex;
        }

        /**
         * Sets the value of the flightPlanIndex property.
         * 
         */
        public void setFlightPlanIndex(short value) {
            this.flightPlanIndex = value;
        }

        /**
         * Gets the value of the routePoint property.
         * 
         * <p>
         * This accessor method returns a reference to the live list,
         * not a snapshot. Therefore any modification you make to the
         * returned list will be present inside the JAXB object.
         * This is why there is not a <CODE>set</CODE> method for the routePoint property.
         * 
         * <p>
         * For example, to add a new item, do as follows:
         * <pre>
         *    getRoutePoint().add(newItem);
         * </pre>
         * 
         * 
         * <p>
         * Objects of the following type(s) are allowed in the list
         * {@link FlightPlan.Route.RoutePoint }
         * 
         * 
         */
        public List<FlightPlan.Route.RoutePoint> getRoutePoint() {
            if (routePoint == null) {
                routePoint = new ArrayList<FlightPlan.Route.RoutePoint>();
            }
            return this.routePoint;
        }


        /**
         * <p>Java class for anonymous complex type.
         * 
         * <p>The following schema fragment specifies the expected content contained within this class.
         * 
         * <pre>
         * &lt;complexType>
         *   &lt;complexContent>
         *     &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
         *       &lt;sequence>
         *         &lt;element name="waypoint-identifier" type="{http://www.w3.org/2001/XMLSchema}string"/>
         *         &lt;element name="waypoint-type" type="{http://www.w3.org/2001/XMLSchema}string"/>
         *         &lt;element name="waypoint-country-code" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
         *       &lt;/sequence>
         *     &lt;/restriction>
         *   &lt;/complexContent>
         * &lt;/complexType>
         * </pre>
         * 
         * 
         */
        @XmlAccessorType(XmlAccessType.FIELD)
        @XmlType(name = "", propOrder = {
            "waypointIdentifier",
            "waypointType",
            "waypointCountryCode"
        })
        public static class RoutePoint {

            @XmlElement(name = "waypoint-identifier", required = true)
            protected String waypointIdentifier;
            @XmlElement(name = "waypoint-type", required = true)
            protected String waypointType;
            @XmlElement(name = "waypoint-country-code")
            protected String waypointCountryCode;

            /**
             * Gets the value of the waypointIdentifier property.
             * 
             * @return
             *     possible object is
             *     {@link String }
             *     
             */
            public String getWaypointIdentifier() {
                return waypointIdentifier;
            }

            /**
             * Sets the value of the waypointIdentifier property.
             * 
             * @param value
             *     allowed object is
             *     {@link String }
             *     
             */
            public void setWaypointIdentifier(String value) {
                this.waypointIdentifier = value;
            }

            /**
             * Gets the value of the waypointType property.
             * 
             * @return
             *     possible object is
             *     {@link String }
             *     
             */
            public String getWaypointType() {
                return waypointType;
            }

            /**
             * Sets the value of the waypointType property.
             * 
             * @param value
             *     allowed object is
             *     {@link String }
             *     
             */
            public void setWaypointType(String value) {
                this.waypointType = value;
            }

            /**
             * Gets the value of the waypointCountryCode property.
             * 
             * @return
             *     possible object is
             *     {@link String }
             *     
             */
            public String getWaypointCountryCode() {
                return waypointCountryCode;
            }

            /**
             * Sets the value of the waypointCountryCode property.
             * 
             * @param value
             *     allowed object is
             *     {@link String }
             *     
             */
            public void setWaypointCountryCode(String value) {
                this.waypointCountryCode = value;
            }

        }

    }


    /**
     * <p>Java class for anonymous complex type.
     * 
     * <p>The following schema fragment specifies the expected content contained within this class.
     * 
     * <pre>
     * &lt;complexType>
     *   &lt;complexContent>
     *     &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
     *       &lt;sequence>
     *         &lt;element name="waypoint" maxOccurs="unbounded">
     *           &lt;complexType>
     *             &lt;complexContent>
     *               &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
     *                 &lt;sequence>
     *                   &lt;element name="identifier" type="{http://www.w3.org/2001/XMLSchema}string"/>
     *                   &lt;element name="type" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
     *                   &lt;element name="country-code" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
     *                   &lt;element name="lat" type="{http://www.w3.org/2001/XMLSchema}decimal"/>
     *                   &lt;element name="lon" type="{http://www.w3.org/2001/XMLSchema}decimal"/>
     *                   &lt;element name="comment" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
     *                   &lt;element name="elevation" type="{http://www.w3.org/2001/XMLSchema}decimal" minOccurs="0"/>
     *                 &lt;/sequence>
     *               &lt;/restriction>
     *             &lt;/complexContent>
     *           &lt;/complexType>
     *         &lt;/element>
     *       &lt;/sequence>
     *     &lt;/restriction>
     *   &lt;/complexContent>
     * &lt;/complexType>
     * </pre>
     * 
     * 
     */
    @XmlAccessorType(XmlAccessType.FIELD)
    @XmlType(name = "", propOrder = {
        "waypoint"
    })
    public static class WaypointTable {

        @XmlElement(required = true)
        protected List<FlightPlan.WaypointTable.Waypoint> waypoint;

        /**
         * Gets the value of the waypoint property.
         * 
         * <p>
         * This accessor method returns a reference to the live list,
         * not a snapshot. Therefore any modification you make to the
         * returned list will be present inside the JAXB object.
         * This is why there is not a <CODE>set</CODE> method for the waypoint property.
         * 
         * <p>
         * For example, to add a new item, do as follows:
         * <pre>
         *    getWaypoint().add(newItem);
         * </pre>
         * 
         * 
         * <p>
         * Objects of the following type(s) are allowed in the list
         * {@link FlightPlan.WaypointTable.Waypoint }
         * 
         * 
         */
        public List<FlightPlan.WaypointTable.Waypoint> getWaypoint() {
            if (waypoint == null) {
                waypoint = new ArrayList<FlightPlan.WaypointTable.Waypoint>();
            }
            return this.waypoint;
        }


        /**
         * <p>Java class for anonymous complex type.
         * 
         * <p>The following schema fragment specifies the expected content contained within this class.
         * 
         * <pre>
         * &lt;complexType>
         *   &lt;complexContent>
         *     &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
         *       &lt;sequence>
         *         &lt;element name="identifier" type="{http://www.w3.org/2001/XMLSchema}string"/>
         *         &lt;element name="type" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
         *         &lt;element name="country-code" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
         *         &lt;element name="lat" type="{http://www.w3.org/2001/XMLSchema}decimal"/>
         *         &lt;element name="lon" type="{http://www.w3.org/2001/XMLSchema}decimal"/>
         *         &lt;element name="comment" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
         *         &lt;element name="elevation" type="{http://www.w3.org/2001/XMLSchema}decimal" minOccurs="0"/>
         *       &lt;/sequence>
         *     &lt;/restriction>
         *   &lt;/complexContent>
         * &lt;/complexType>
         * </pre>
         * 
         * 
         */
        @XmlAccessorType(XmlAccessType.FIELD)
        @XmlType(name = "", propOrder = {
            "identifier",
            "type",
            "countryCode",
            "lat",
            "lon",
            "comment",
            "elevation"
        })
        public static class Waypoint {

            @XmlElement(required = true)
            protected String identifier;
            protected String type;
            @XmlElement(name = "country-code")
            protected String countryCode;
            @XmlElement(required = true)
            protected BigDecimal lat;
            @XmlElement(required = true)
            protected BigDecimal lon;
            protected String comment;
            protected BigDecimal elevation;

            /**
             * Gets the value of the identifier property.
             * 
             * @return
             *     possible object is
             *     {@link String }
             *     
             */
            public String getIdentifier() {
                return identifier;
            }

            /**
             * Sets the value of the identifier property.
             * 
             * @param value
             *     allowed object is
             *     {@link String }
             *     
             */
            public void setIdentifier(String value) {
                this.identifier = value;
            }

            /**
             * Gets the value of the type property.
             * 
             * @return
             *     possible object is
             *     {@link String }
             *     
             */
            public String getType() {
                return type;
            }

            /**
             * Sets the value of the type property.
             * 
             * @param value
             *     allowed object is
             *     {@link String }
             *     
             */
            public void setType(String value) {
                this.type = value;
            }

            /**
             * Gets the value of the countryCode property.
             * 
             * @return
             *     possible object is
             *     {@link String }
             *     
             */
            public String getCountryCode() {
                return countryCode;
            }

            /**
             * Sets the value of the countryCode property.
             * 
             * @param value
             *     allowed object is
             *     {@link String }
             *     
             */
            public void setCountryCode(String value) {
                this.countryCode = value;
            }

            /**
             * Gets the value of the lat property.
             * 
             * @return
             *     possible object is
             *     {@link BigDecimal }
             *     
             */
            public BigDecimal getLat() {
                return lat;
            }

            /**
             * Sets the value of the lat property.
             * 
             * @param value
             *     allowed object is
             *     {@link BigDecimal }
             *     
             */
            public void setLat(BigDecimal value) {
                this.lat = value;
            }

            /**
             * Gets the value of the lon property.
             * 
             * @return
             *     possible object is
             *     {@link BigDecimal }
             *     
             */
            public BigDecimal getLon() {
                return lon;
            }

            /**
             * Sets the value of the lon property.
             * 
             * @param value
             *     allowed object is
             *     {@link BigDecimal }
             *     
             */
            public void setLon(BigDecimal value) {
                this.lon = value;
            }

            /**
             * Gets the value of the comment property.
             * 
             * @return
             *     possible object is
             *     {@link String }
             *     
             */
            public String getComment() {
                return comment;
            }

            /**
             * Sets the value of the comment property.
             * 
             * @param value
             *     allowed object is
             *     {@link String }
             *     
             */
            public void setComment(String value) {
                this.comment = value;
            }

            /**
             * Gets the value of the elevation property.
             * 
             * @return
             *     possible object is
             *     {@link BigDecimal }
             *     
             */
            public BigDecimal getElevation() {
                return elevation;
            }

            /**
             * Sets the value of the elevation property.
             * 
             * @param value
             *     allowed object is
             *     {@link BigDecimal }
             *     
             */
            public void setElevation(BigDecimal value) {
                this.elevation = value;
            }

        }

    }

}
