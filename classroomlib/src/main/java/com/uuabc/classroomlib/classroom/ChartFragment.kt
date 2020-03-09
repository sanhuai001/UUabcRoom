package com.uuabc.classroomlib.classroom

import android.animation.Animator
import android.annotation.SuppressLint
import android.os.Bundle
import android.os.Handler
import android.os.Message
import android.text.Html
import android.text.TextUtils
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import androidx.viewpager.widget.PagerAdapter
import androidx.viewpager.widget.ViewPager
import com.blankj.utilcode.util.FileIOUtils
import com.blankj.utilcode.util.SPUtils
import com.bumptech.glide.Glide
import com.bumptech.glide.request.RequestOptions
import com.uuabc.classroomlib.R
import com.uuabc.classroomlib.RoomApplication
import com.uuabc.classroomlib.RoomConstant
import com.uuabc.classroomlib.common.BaseCommonFragment
import com.uuabc.classroomlib.common.RxTimer
import com.uuabc.classroomlib.databinding.FragmentRoomSdkChartListBinding
import com.uuabc.classroomlib.db.RoomDbManager
import com.uuabc.classroomlib.model.SocketModel.ChartModel
import com.uuabc.classroomlib.model.db.LottieRecord
import com.uuabc.classroomlib.utils.CompatUtil
import com.uuabc.classroomlib.utils.ObjectUtil
import com.uuabc.classroomlib.utils.SocketIoUtils
import com.uuabc.classroomlib.widget.LowBdapter
import io.reactivex.Observable
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.schedulers.Schedulers
import kotlinx.android.synthetic.main.fragment_room_sdk_chart_list.*
import kotlinx.android.synthetic.main.item_room_sdk_emoji.view.*
import kotlinx.android.synthetic.main.item_room_sdk_emoji_viewpager.view.*
import java.util.*

@SuppressLint("StaticFieldLeak", "HandlerLeak")
open class ChartFragment : BaseCommonFragment() {
    private var binding: FragmentRoomSdkChartListBinding? = null
    private var adapter: ChartListAdapter? = null
    private lateinit var emojis: MutableList<LottieRecord>
    private var emojiItemAdapter: PagerAdapter? = null

    private val handler = object : Handler() {
        override fun handleMessage(msg: Message) {
            binding!!.tvTips.animate().translationX(0f).translationX((-binding!!.tvTips.width).toFloat()).setListener(object : Animator.AnimatorListener {
                override fun onAnimationStart(animation: Animator) {

                }

                override fun onAnimationEnd(animation: Animator) {
                    binding!!.tvTips.visibility = View.GONE
                    binding!!.tvTips.translationX = 0f
                }

                override fun onAnimationCancel(animation: Animator) {
                    binding!!.tvTips.visibility = View.GONE
                    binding!!.tvTips.translationX = 0f
                }

                override fun onAnimationRepeat(animation: Animator) {

                }
            }).setDuration(300).start()
        }
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        binding = DataBindingUtil.inflate(inflater, R.layout.fragment_room_sdk_chart_list, container, false)
        initView()
        initEmojis()
        return binding!!.root
    }

    private fun initView() {
        val manager = LinearLayoutManager(mContext)
        manager.orientation = RecyclerView.VERTICAL
        binding!!.rvChatList.layoutManager = manager
        adapter = ChartListAdapter(mContext)
        binding!!.rvChatList.adapter = adapter
    }

    private fun initEmojis() {
        emojis = RoomDbManager.getInstance().queryAllLottieRecords()
        if (emojis.isEmpty()) return
        if (emojiItemAdapter == null) {
            emojiItemAdapter = object : PagerAdapter() {
                override fun instantiateItem(container: ViewGroup, position: Int): Any {
                    val v = View.inflate(mContext, R.layout.item_room_sdk_emoji_viewpager, null)
                    container.addView(v)
                    val subEmoji = emojis.subList(8 * position, (if (8 * (position + 1) > emojis.size) {
                        emojis.size
                    } else {
                        8 * (position + 1)
                    }))

                    initEmojiAdapter(v, subEmoji)
                    return v
                }

                override fun isViewFromObject(view: View, `object`: Any): Boolean {
                    return view === `object`
                }

                override fun destroyItem(container: ViewGroup, position: Int, `object`: Any) {
                    if (`object` is View)
                        container.removeView(`object`)
                }

                override fun getCount(): Int {
                    return if (emojis.size <= 8) 1 else emojis.size / 8 + 1
                }
            }
        }

        RxTimer().timer(1000) {
            indicator.visibility = View.VISIBLE
            indicator.setRadius(5f)
            indicator.padding = 6
            indicator.selectedColor = CompatUtil.getColor(context, R.color.colorAccent)
            indicator.unselectedColor = CompatUtil.getColor(context, R.color.white)
            vpEmojiList.adapter = emojiItemAdapter
            indicator.count = emojiItemAdapter?.count!!
            vpEmojiList.addOnPageChangeListener(object : ViewPager.OnPageChangeListener {
                override fun onPageScrollStateChanged(state: Int) {
                }

                override fun onPageScrolled(position: Int, positionOffset: Float, positionOffsetPixels: Int) {
                }

                override fun onPageSelected(position: Int) {
                    indicator.selection = position
                }
            })
        }
    }

    private fun initEmojiAdapter(v: View, subEmoji: MutableList<LottieRecord>) {
        val manager = GridLayoutManager(mContext, 4)
        v.rvItemEmojiList.layoutManager = manager
        val emojiAdapter = object : LowBdapter<LottieRecord, LowBdapter.Holder>(mContext, subEmoji) {

            override fun bindView(holder: Holder, position: Int, data: MutableList<LottieRecord>) {
                Glide.with(context).load(data[position].smallImgPath).apply(RequestOptions())
                        .into(holder.itemView.ivItemEmoji)
            }

            override fun getLayoutId(viewType: Int): Int {
                return R.layout.item_room_sdk_emoji
            }

            override fun createHolder(view: View): Holder {
                return object : Holder(view) {
                    init {
                        view.clItemEmoji.setOnClickListener { _ ->
                            sendEmoji(data[adapterPosition])
                        }
                    }
                }
            }
        }
        v.rvItemEmojiList.adapter = emojiAdapter
    }

    /***
     * 发送表情
     */
    @SuppressLint("CheckResult")
    private fun sendEmoji(lottieRecord: LottieRecord) {
        var json = ""
        Observable.just("getLottieJson")
                .subscribeOn(Schedulers.io())
                .doOnNext { _ -> json = FileIOUtils.readFile2String(lottieRecord.lottieJsonPath) }
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe({ _ ->
                    var name = SPUtils.getInstance().getString(RoomConstant.ENGLISH_NAME, "")
                    if (TextUtils.isEmpty(name))
                        name = SPUtils.getInstance().getString(RoomConstant.USER_NAME, "")
                    val chartModel = ChartModel()
                    chartModel.userName = name
                    chartModel.type = 4
                    chartModel.sendId = ObjectUtil.getString(SPUtils.getInstance().getInt(RoomConstant.USER_ID))
                    chartModel.msg = json
                    addChartModel(chartModel, false)
                    sendChat(lottieRecord.code)
                }, { _ -> })
    }

    fun sendChat(value: String) {
        val chat = HashMap<String, Any>()
        chat["event"] = RoomConstant.CHAT
        chat["data"] = value
        RoomApplication.getInstance().sendMessage(RoomConstant.SHARE, SocketIoUtils.getJsonObject(chat))
    }

    fun setChartModel(chartModels: List<ChartModel>?) {
        if (!isAdded) return
        binding!!.ivChatNodata.visibility = if (chartModels == null || chartModels.isEmpty()) View.VISIBLE else View.GONE
        adapter!!.addData(chartModels)
        binding!!.rvChatList.scrollToPosition(adapter!!.itemCount - 1)
    }

    @SuppressLint("CheckResult")
    fun addChartModel(chartModel: ChartModel?, needCheck: Boolean = false) {
        if (!isAdded) return
        binding!!.ivChatNodata.visibility = if (chartModel == null) View.VISIBLE else View.GONE
        if (needCheck) {
            if (!::emojis.isInitialized) {
                emojis = RoomDbManager.getInstance().queryAllLottieRecords()
            }

            emojis.forEach {
                if (it.code == chartModel?.msg) {
                    var json = ""
                    Observable.just("getLottieJson")
                            .subscribeOn(Schedulers.io())
                            .doOnNext { _ -> json = FileIOUtils.readFile2String(it.lottieJsonPath) }
                            .observeOn(AndroidSchedulers.mainThread())
                            .subscribe({ _ ->
                                chartModel?.type = 5
                                chartModel?.msg = json
                                adapter!!.addData(chartModel)
                                binding!!.rvChatList.scrollToPosition(adapter!!.itemCount - 1)
                            }, { _ -> })
                    return
                }
            }
        }
        adapter!!.addData(chartModel)
        binding!!.rvChatList.scrollToPosition(adapter!!.itemCount - 1)
    }

    fun newUserRoomIn(userName: String) {
        handler.removeMessages(0)
        binding!!.tvTips.visibility = View.VISIBLE
        binding!!.tvTips.text = Html.fromHtml(mContext.resources.getString(R.string.fragment_class_in_tips_str, userName))
        handler.sendEmptyMessageDelayed(0, 1500)
    }

    companion object {
        private var fragment: ChartFragment? = null

        fun newInstant(): ChartFragment {
            if (fragment == null) {
                fragment = ChartFragment()
            }
            return fragment as ChartFragment
        }
    }
}
