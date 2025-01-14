package net.gbs.epp_project.Ui.Audit.CycleCount.OnHand

import android.app.Activity
import android.app.Application
import androidx.lifecycle.ViewModel
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import net.gbs.epp_project.Base.BaseViewModel
import net.gbs.epp_project.Model.ApiRequestBody.MobileLogBody
import net.gbs.epp_project.Model.ItemCompare
import net.gbs.epp_project.Model.OnHandItem
import net.gbs.epp_project.Model.Status
import net.gbs.epp_project.Model.StatusWithMessage
import net.gbs.epp_project.R
import net.gbs.epp_project.Repositories.AuditRepository
import net.gbs.epp_project.Tools.ResponseDataHandler
import net.gbs.epp_project.Tools.SingleLiveEvent
import net.gbs.epp_project.Ui.SplashAndSignIn.SignInFragment.Companion.USER

class OnHandViewModel(private val application: Application,val activity: Activity) : BaseViewModel(application, activity) {
    private val repository = AuditRepository(activity)
    val getOnHandsItemsLiveData = SingleLiveEvent<List<ItemCompare>>()
    val getOnHandsItemsStatus   = SingleLiveEvent<StatusWithMessage>()
    fun getOnHandsItems(headerId:Int,orgCode:String){
        getOnHandsItemsStatus.postValue(StatusWithMessage(Status.LOADING))
        job = CoroutineScope(Dispatchers.IO).launch {
            try {
                val response = repository.getOnHands(headerId,orgCode)
                ResponseDataHandler(response,getOnHandsItemsLiveData,getOnHandsItemsStatus,application).handleData("GetCycleCountOrder_StockCompare")
                if (response.body()?.responseStatus?.errorMessage!=null)
                    repository.MobileLog(
                        MobileLogBody(
                            userId = USER?.notOracleUserId,
                            errorMessage = response.body()?.responseStatus?.errorMessage,
                            apiName = "GetCycleCountOrder_StockCompare"
                        )
                    )
            } catch (ex:Exception){
                getOnHandsItemsStatus.postValue(StatusWithMessage(Status.NETWORK_FAIL,application.getString(
                    R.string.error_in_connection)))
            }
        }
    }
}