package yc.com.chat.tools.activity

import android.content.DialogInterface
import android.content.Intent
import android.support.v7.app.AlertDialog
import android.text.TextUtils
import android.view.View
import com.jakewharton.rxbinding.view.RxView
import com.kk.utils.ToastUtil
import kotlinx.android.synthetic.main.activity_create_wxtx.*
import kotlinx.android.synthetic.main.top_include.*
import yc.com.base.BaseActivity
import yc.com.base.BaseEngine
import yc.com.base.BasePresenter
import yc.com.base.BaseView
import yc.com.chat.R
import yc.com.chat.base.constant.*
import yc.com.chat.constellation.datapicker.DatePickDialog
import yc.com.chat.constellation.datapicker.DateType
import yc.com.chat.constellation.datapicker.OnSureLisener
import java.util.*
import java.util.concurrent.TimeUnit


/**
 *
 * Created by wanglin  on 2019/6/20 16:30.
 */
class CreateWxtxActivity : BaseActivity<BasePresenter<BaseEngine, BaseView>>() {
    private val choose = arrayOf("中国银行", "农业银行", "建设银行", "招商银行", "民生银行", "交通银行", "华夏银行", "工商银行", "邮政储蓄银行", "浦发银行", "农村信用社")

    private var isExit = true


    override fun getLayoutId(): Int {
        return R.layout.activity_create_wxtx
    }

    override fun init() {
//        tv_title.text = "微信提现"
        mainToolbar.setBasicInfo(null, "微信提现", null)
        mainToolbar.init(this)


        initListener()
    }

    fun initListener() {
        RxView.clicks(tv_make).throttleFirst(200, TimeUnit.MILLISECONDS).subscribe {
            val trim = tv_bank.text.toString().trim()
            val trim2 = et_num.text.toString().trim()
            val trim3 = tv_pay_time.text.toString().trim()
            val trim4 = et_money.text.toString().trim()
            var trim5 = et_pay_service.text.toString().trim()
            if (TextUtils.isEmpty(trim5)) {
                trim5 = "0.10"
            }
            if (TextUtils.isEmpty(trim2) || TextUtils.isEmpty(trim4)) {
                ToastUtil.toast2(this, "请填写尾号或金额")

            } else {
                startActivity(Intent(this, WxtxActivity::class.java)
                        .putExtra(moneyNum, trim4)
                        .putExtra(i_payService, trim5).putExtra(i_num, trim2)
                        .putExtra(i_bank, trim).putExtra(i_payTime, trim3).putExtra(i_isExit,isExit))

            }
        }

//        RxView.clicks(rl_back).throttleFirst(200, TimeUnit.MILLISECONDS).subscribe { finish() }

        RxView.clicks(rl_pay_time).throttleFirst(200, TimeUnit.MILLISECONDS).subscribe {
            val datePickDialog = DatePickDialog(this)
            datePickDialog.setYearLimt(18)
            datePickDialog.setTitle("选择时间")
            datePickDialog.setType(DateType.TYPE_YMDHM)
            datePickDialog.setMessageFormat("yyyy-MM-dd HH:mm")
            datePickDialog.setOnChangeLisener(null)
            datePickDialog.setOnSureLisener(C07661())
            datePickDialog.show()
        }

        RxView.clicks(rl_pay_bank).throttleFirst(200, TimeUnit.MILLISECONDS).subscribe {
            val builder = AlertDialog.Builder(this)
            builder.setTitle("选择银行卡" as CharSequence)
            builder.setItems(this.choose, C07672())
            builder.show()
        }

        RxView.clicks(iv_turn).throttleFirst(200, TimeUnit.MILLISECONDS).subscribe {
            isExit = !isExit
            if (isExit) {
                iv_turn.setBackgroundResource(R.mipmap.ipay_ui_radio_group_on)
                et_pay_service.visibility = View.VISIBLE
                return@subscribe
            }
            iv_turn.setBackgroundResource(R.mipmap.ipay_ui_radio_group_off)
            et_pay_service.visibility = View.GONE
        }

    }

    /* renamed from: com.rjjmc.newproinlove.activity.screenshot.CreateWxtxActivity$1 */
    internal inner class C07661 : OnSureLisener {

        override fun onSure(date: Date) {
            tv_pay_time.text = setDate(date)
        }
    }

    /* renamed from: com.rjjmc.newproinlove.activity.screenshot.CreateWxtxActivity$2 */
    internal inner class C07672 : DialogInterface.OnClickListener {

        override fun onClick(dialog: DialogInterface, which: Int) {
            tv_bank.text = choose[which]
        }
    }

    private fun addZero(d: Int): String {
        return if (d < 10) "0$d" else "" + d
    }

    private fun setDate(date: Date): String {
        val year = date.year - 100 + 2000
        val addZero = addZero(date.month + 1)
        val addZero2 = addZero(date.date)
        val addZero3 = addZero(date.hours)
        return "$year-$addZero-$addZero2 $addZero3:${addZero(date.minutes)}"
    }


}