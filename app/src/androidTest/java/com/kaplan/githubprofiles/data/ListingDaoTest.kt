package com.kaplan.githubprofiles.data

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import androidx.test.ext.junit.runners.AndroidJUnit4
import com.kaplan.githubprofiles.ui.detail.data.DetailDao
import com.kaplan.githubprofiles.ui.detail.data.DetailItem
import kotlinx.coroutines.runBlocking
import org.hamcrest.Matchers.equalTo
import org.junit.Assert.assertThat
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith

@RunWith(AndroidJUnit4::class)
class DetailDaoTest : DbTest() {

    val testDetailItemA = DetailItem(1, "userA", "namea", "companyA",
        "locationA", "emailA", "bioA", 1, 1,1,"photoUrlA", "urlA", "starredUrlA",
        "repoUrlA", "blogA", "typeA", "noteA")

    val testDetailItemB = DetailItem(2, "userB", "nameB", "companyB",
        "locationB", "emailB", "bioB", 1, 1,1,"photoUrlB", "urlB", "starredUrlB",
        "repoUrlB", "blogB", "typeB", "noteB")

    private lateinit var detailDao: DetailDao
    private val setA = testDetailItemA.copy()
    private val setB = testDetailItemB.copy()

    @get:Rule
    var instantTaskExecutorRule = InstantTaskExecutorRule()

    @Before fun createDb() {
        detailDao = db.detailDao()

        runBlocking {
            detailDao.insert(setA)
            detailDao.insert(setB)
        }
    }

    @Test fun testCheckDetails() {
        val list = getValue(detailDao.getDetailItems())
        assertThat(list.size, equalTo(2))

        assertThat(list[0], equalTo(setA))
        assertThat(list[1], equalTo(setB))
    }

    @Test fun testGetDetail() {
        assertThat(getValue(detailDao.getDetailItem(setA.login)), equalTo(setA))
    }
}