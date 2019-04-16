package mcgill.sharoz.belltest

import mcgill.sharoz.belltest.util.Utils.formatDate
import org.hamcrest.CoreMatchers.`is`
import org.junit.Assert.assertThat
import org.junit.Test

class FormatDateTest {

    @Test
    fun inputEmpty_ReturnEmptyString() {
        assertThat(formatDate(""), `is`(""))
    }

    @Test
    fun inputCorrectDate_ReturnCorrectFormattedDate() {
        assertThat(formatDate("Mon Apr 15 10:29:27 +0000 2019"), `is`("15 Apr 2019"))
        assertThat(formatDate("Sat Oct 15 15:35:22 +0000 2011"), `is`("15 Oct 2011"))
        assertThat(formatDate("Mon Jan 05 22:20:24 +0000 2022"), `is`("05 Jan 2022"))
    }

    @Test
    fun inputCorrectDate_ReturnFormattedDateCorrectUTC(){
        // making sure that the date do not change due to local timezone
        assertThat(formatDate("Sat Aug 30 00:21:14 +0000 2015"), `is`("30 Aug 2015"))
        assertThat(formatDate("Tue May 29 23:37:51 +0000 1990"), `is`("29 May 1990"))
    }

    @Test
    fun inputWrongString_ReturnEmptyString() {
        assertThat(formatDate("Some String"), `is`(""))
        assertThat(formatDate("   ABC 123 "), `is`(""))
        assertThat(formatDate("Mon Apr 15 10:29:27"), `is`(""))
        assertThat(formatDate("10:29:27 +0000 2019"), `is`(""))
        assertThat(formatDate("invalid Date input"), `is`(""))
        assertThat(formatDate("this is another string"), `is`(""))
    }

}
