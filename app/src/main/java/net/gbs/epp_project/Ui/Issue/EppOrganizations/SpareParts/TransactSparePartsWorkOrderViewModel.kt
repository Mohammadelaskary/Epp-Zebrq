package net.gbs.epp_project.Ui.Issue.EppOrganizations.SpareParts

import android.app.Activity
import android.app.Application
import android.content.ContentValues
import android.content.ContentValues.TAG
import android.util.Log
import androidx.lifecycle.MutableLiveData
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import net.gbs.epp_project.Base.BaseViewModel
import net.gbs.epp_project.Model.ApiRequestBody.TransactItemsBody
import net.gbs.epp_project.Model.IssueOrderLists
import net.gbs.epp_project.Model.Locator
import net.gbs.epp_project.Model.MoveOrderLine
import net.gbs.epp_project.Model.Organization
import net.gbs.epp_project.Model.Status
import net.gbs.epp_project.Model.StatusWithMessage
import net.gbs.epp_project.Model.SubInventory
import net.gbs.epp_project.Model.WorkOrderOrder
import net.gbs.epp_project.R
import net.gbs.epp_project.Repositories.IssueRepository
import net.gbs.epp_project.Tools.ResponseDataHandler
import net.gbs.epp_project.Tools.ResponseHandler
import net.gbs.epp_project.Tools.SingleLiveEvent

class TransactSparePartsWorkOrderViewModel(private val application: Application, val activity: Activity) : BaseViewModel(application,activity) {
    var moveOrder: WorkOrderOrder? = null
    private val issueRepository = IssueRepository(activity)

    val getMoveOrderLinesLiveData = SingleLiveEvent<List<MoveOrderLine>>()
    val getMoveOrderLinesStatus   = SingleLiveEvent<StatusWithMessage>()
    fun getMoveOrderLines(workOrderName:String?,orgId:Int){
        job = CoroutineScope(Dispatchers.IO).launch {
            getMoveOrderLinesStatus.postValue(StatusWithMessage(Status.LOADING))
            try {
                val response = issueRepository.getMoveOrderLinesByWorkOrderName(workOrderName, orgId)
                ResponseDataHandler(response,getMoveOrderLinesLiveData,getMoveOrderLinesStatus,application).handleData()
            } catch (ex:Exception){
                getMoveOrderLinesStatus.postValue(StatusWithMessage(Status.NETWORK_FAIL,application.getString(
                    R.string.error_in_getting_data)))
                Log.d(TAG, "===ErrorgetMoveOrderLines: ${ex.message}")
            }
        }
    }
    val getSubInvertoryListLiveData = SingleLiveEvent<List<SubInventory>>()
    val getSubInvertoryListStatus   = SingleLiveEvent<StatusWithMessage>()
    fun getSubInvertoryList(orgId:Int){
        job = CoroutineScope(Dispatchers.IO).launch {
            getSubInvertoryListStatus.postValue(StatusWithMessage(Status.LOADING))
            try {
                val response = issueRepository.getSubInventoryList(orgId)
                ResponseDataHandler(response,getSubInvertoryListLiveData,getSubInvertoryListStatus,application).handleData()
            } catch (ex:Exception){
                getSubInvertoryListStatus.postValue(StatusWithMessage(Status.NETWORK_FAIL,application.getString(
                    R.string.error_in_getting_data)))
                Log.d(TAG, "===ErrorgetSubInvertoryList: ${ex.message}")
            }
        }
    }

    val getLocatorsListLiveData = SingleLiveEvent<List<Locator>>()
    val getLocatorsListStatus   = SingleLiveEvent<StatusWithMessage>()
    fun getLocatorsList(orgId:Int,subInvCode:String){
        job = CoroutineScope(Dispatchers.IO).launch {
            getLocatorsListStatus.postValue(StatusWithMessage(Status.LOADING))
            try {
                val response = issueRepository.getLocatorList(orgId,subInvCode)
                ResponseDataHandler(response,getLocatorsListLiveData,getLocatorsListStatus,application).handleData()
            } catch (ex:Exception){
                getLocatorsListStatus.postValue(StatusWithMessage(Status.NETWORK_FAIL,application.getString(
                    R.string.error_in_getting_data)))
                Log.d(TAG, "===ErrorgetLocatorsList: ${ex.message}")
            }
        }
    }
    
    val allocateItemsStatus = SingleLiveEvent<StatusWithMessage>()
    fun transactItems(body: TransactItemsBody){
        allocateItemsStatus.postValue(StatusWithMessage(Status.LOADING))
        job = CoroutineScope(Dispatchers.IO).launch {
            try {
                val response = issueRepository.transactItems(body)
                ResponseHandler(response,allocateItemsStatus,application).handleData()
            } catch (ex:Exception){
                allocateItemsStatus.postValue(StatusWithMessage(Status.NETWORK_FAIL,application.getString(R.string.error_in_connection)))
            }
        }
    }


    val getWorkOrdersListLiveData = SingleLiveEvent<List<WorkOrderOrder>>()
    val getWorkOrdersListStatus   = SingleLiveEvent<StatusWithMessage>()
    fun getWorkOrdersList(orgId:Int){
        job = CoroutineScope(Dispatchers.IO).launch {
            getWorkOrdersListStatus.postValue(StatusWithMessage(Status.LOADING))
            try {
                val response = issueRepository.getWorkOrdersList(orgId)
                ResponseDataHandler(response,getWorkOrdersListLiveData,getWorkOrdersListStatus,application).handleData()
            } catch (ex:Exception){
                getWorkOrdersListStatus.postValue(
                    StatusWithMessage(
                        Status.NETWORK_FAIL,application.getString(
                            R.string.error_in_getting_data))
                )
            }
        }
    }
    fun getJobOrdersList(orgId:Int){
        job = CoroutineScope(Dispatchers.IO).launch {
            getWorkOrdersListStatus.postValue(StatusWithMessage(Status.LOADING))
            try {
                val response = issueRepository.getJobOrdersList(orgId)
                ResponseDataHandler(response,getWorkOrdersListLiveData,getWorkOrdersListStatus,application).handleData()
            } catch (ex:Exception){
                getWorkOrdersListStatus.postValue(
                    StatusWithMessage(
                        Status.NETWORK_FAIL,application.getString(
                            R.string.error_in_getting_data))
                )
            }
        }
    }
    val getIssueOrdersListLiveData = SingleLiveEvent<IssueOrderLists>()
    val getIssueOrdersListStatus   = SingleLiveEvent<StatusWithMessage>()
    fun getIssueOrderLists(requestNumber:String,orgId: Int){
        job = CoroutineScope(Dispatchers.IO).launch {
            getIssueOrdersListStatus.postValue(StatusWithMessage(Status.LOADING))
            try {
                val response = issueRepository.getIssueOrdersList(requestNumber,orgId)
                ResponseDataHandler(response,getIssueOrdersListLiveData,getIssueOrdersListStatus,application).handleData()
            } catch (ex:Exception){
                getIssueOrdersListStatus.postValue(StatusWithMessage(Status.NETWORK_FAIL,application.getString(
                    R.string.error_in_getting_data)))
                Log.d(ContentValues.TAG, "===ErrorgetLocatorsList: ${ex.message}")
            }
        }
    }

    fun getTodayDate() = issueRepository.getTodayDate()
    fun getDisplayTodayDate()= issueRepository.getTodayDate().substring(0,10)
//    fun getTodayFullDate() = issueRepository.getStoredActualFullDateTime()

    val getOrganizationsListLiveData = SingleLiveEvent<List<Organization>>()
    val getOrganizationsListStatus = SingleLiveEvent<StatusWithMessage>()
    fun getOrganizationsList(){
        getOrganizationsListStatus.postValue(StatusWithMessage(Status.LOADING))
        job = CoroutineScope(Dispatchers.IO).launch {
            try {
                val response = issueRepository.getOrganizations()
                ResponseDataHandler(response,getOrganizationsListLiveData,getOrganizationsListStatus,application).handleData()
            } catch (ex:Exception){
                getOrganizationsListStatus.postValue(
                    StatusWithMessage(
                        Status.NETWORK_FAIL,application.getString(
                            R.string.error_in_connection))
                )
            }
        }
    }
}