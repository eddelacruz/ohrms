package ws.helper

import org.joda.time._

/**
 * Created with IntelliJ IDEA.
 * User: Elizer
 * Date: 12/11/12
 * Time: 5:13 PM
 * To change this template use File | Settings | File Templates.
 */
object DateWithTime {

  def dateNow: String = {
    //var a = new DateTime.parse(DateTime.now.getYear+"-"+DateTime.now.getMonthOfYear+"-"+DateTime.now.getDayOfMonth+" "+DateTime.now.getHourOfDay+":"+DateTime.now.getMinuteOfHour+":"+DateTime.now.getSecondOfMinute
    DateTime.now.toDateTimeISO.toLocalDate.toString+" "+DateTime.now.toDateTimeISO.toLocalTime.toString
  }

  def dateNowAllDay: String = {
    DateTime.now.getYear+"-"+DateTime.now.getMonthOfYear+"-"+DateTime.now.getDayOfMonth+" 00:00:00"
  }

  def dateOnly: String = {
    DateTime.now.getYear+"-"+DateTime.now.getMonthOfYear+"-"+DateTime.now.getDayOfMonth
  }

}
