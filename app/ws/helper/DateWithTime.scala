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
    DateTime.now.getYear+"-"+DateTime.now.getMonthOfYear+"-"+DateTime.now.getDayOfMonth+" "+DateTime.now.getHourOfDay+":"+DateTime.now.getMinuteOfHour+":"+DateTime.now.getSecondOfMinute
  }

}
