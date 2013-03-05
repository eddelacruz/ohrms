package ws.helper

import org.joda.time._
import format.{PeriodFormatter, PeriodFormatterBuilder, DateTimeFormat, DateTimeFormatter}

/**
 * Created with IntelliJ IDEA.
 * User: Elizer
 * Date: 12/11/12
 * Time: 5:13 PM
 * To change this template use File | Settings | File Templates.
 */
object DateWithTime {

  val monthArr: Array[String] = Array("January", "February", "March", "April", "May", "June", "July", "August", "September", "October", "November", "December")

  def dateNow: String = {
    //var a = new DateTime.parse(DateTime.now.getYear+"-"+DateTime.now.getMonthOfYear+"-"+DateTime.now.getDayOfMonth+" "+DateTime.now.getHourOfDay+":"+DateTime.now.getMinuteOfHour+":"+DateTime.now.getSecondOfMinute
    val time = DateTime.now.toDateTimeISO.toLocalTime.getHourOfDay+":"+DateTime.now.toDateTimeISO.toLocalTime.getMinuteOfHour+":"+DateTime.now.toDateTimeISO.toLocalTime.getSecondOfMinute
    DateTime.now.toDateTimeISO.toLocalDate.toString+" "+time
  }

  def dateNowAllDay: String = {
    DateTime.now.getYear+"-"+DateTime.now.getMonthOfYear+"-"+DateTime.now.getDayOfMonth+" 00:00:00"
  }

  def dateEndOfDay: String = {
    DateTime.now.getYear+"-"+DateTime.now.getMonthOfYear+"-"+DateTime.now.getDayOfMonth+" 23:59:59"
  }

  def dateOnly: String = {
    DateTime.now.getYear+"-"+DateTime.now.getMonthOfYear+"-"+DateTime.now.getDayOfMonth
  }

  def dateWithTimeString: String = {
    monthArr(DateTime.now.getMonthOfYear-1)+" "+DateTime.now.getDayOfMonth+", "+DateTime.now.getYear
  }

  def getNumberOfDays(year: Int, month: Int): Int = {
    DateTime.parse(year+"-"+month).dayOfMonth.getMaximumValue
  }

}
