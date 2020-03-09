package com.uuabc.classroomlib.widget.dialog

import android.content.Context
import android.graphics.drawable.Drawable
import android.view.LayoutInflater
import android.view.ViewGroup
import android.widget.ImageView
import androidx.recyclerview.widget.GridLayoutManager
import com.blankj.utilcode.util.SPUtils
import com.blankj.utilcode.util.ScreenUtils
import com.blankj.utilcode.util.SizeUtils
import com.bumptech.glide.Glide
import com.bumptech.glide.request.RequestOptions
import com.uuabc.classroomlib.R
import com.uuabc.classroomlib.RoomApplication
import com.uuabc.classroomlib.RoomConstant
import com.uuabc.classroomlib.model.SocketModel.UserModel
import com.uuabc.classroomlib.utils.ObjectUtil
import com.uuabc.classroomlib.widget.AlphaAdapter
import com.uuabc.classroomlib.widget.CaptainDialog
import com.uuabc.classroomlib.widget.GridSpacingItemDecoration
import kotlinx.android.synthetic.main.dialog_room_sdk_setting.view.*
import kotlinx.android.synthetic.main.item_adjust_voice.view.*
import org.json.JSONObject
import java.util.*
import kotlin.collections.HashMap
import kotlin.collections.set

open class SettingDialog(context: Context) : BaseDialog(context) {

    var voiceJsonObj: com.alibaba.fastjson.JSONObject? = null
    var clickListener: OnClickListener? = null
    var adapter: AlphaAdapter<UserModel, AlphaAdapter.Holder>? = null
    var volumeMap = HashMap<String, Int>()
    var studentList = ArrayList<UserModel>()
    private var volumes: Array<Drawable>? = null
    private var roomId = 0

    interface OnClickListener {
        fun onClick()
    }

    init {
        this.context = context

        volumes = arrayOf(
                context.resources.getDrawable(R.drawable.ic_setting_volume_level_0, null),
                context.resources.getDrawable(R.drawable.ic_setting_volume_level_1, null),
                context.resources.getDrawable(R.drawable.ic_setting_volume_level_2, null),
                context.resources.getDrawable(R.drawable.ic_setting_volume_level_3, null),
                context.resources.getDrawable(R.drawable.ic_setting_volume_level_4, null),
                context.resources.getDrawable(R.drawable.ic_setting_volume_level_5, null),
                context.resources.getDrawable(R.drawable.ic_setting_volume_level_6, null),
                context.resources.getDrawable(R.drawable.ic_setting_volume_level_7, null),
                context.resources.getDrawable(R.drawable.ic_setting_volume_level_8, null),
                context.resources.getDrawable(R.drawable.ic_setting_volume_level_9, null),
                context.resources.getDrawable(R.drawable.ic_setting_volume_level_10, null)
        )

        builder = CaptainDialog.Builder(context)
        val dialogHeight = ScreenUtils.getScreenHeight() * 0.7
        val dialogWidth = dialogHeight * 1.365
        dialog = builder.cancelTouchout(false)
                .view(R.layout.dialog_room_sdk_setting)
                .widthpx(dialogWidth.toInt())
                .heightpx(dialogHeight.toInt())
                .style(R.style.Dialog_No_Title)
                .build()
        dialog.window!!.setBackgroundDrawableResource(R.color.transparent)
        adapter = object : AlphaAdapter<UserModel, AlphaAdapter.Holder>(context) {
            override fun bindView(holder: Holder, position: Int, data: MutableList<UserModel>) {
                val student = data[position]
                holder.itemView.ivAvatar.loadImage(student.photo)
                holder.itemView.tvName.text = student.name
                setVoiceLevel(holder.itemView.ivVolume, student.volumeLevel)
            }

            override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): Holder {
                return Holder(LayoutInflater.from(context).inflate(R.layout.item_adjust_voice, parent, false)).apply {
                    itemView.ivAdd.setOnClickListener {
                        if (data[adapterPosition].volumeLevel + 10 >= 100) {
                            data[adapterPosition].volumeLevel = 100
                        } else {
                            data[adapterPosition].volumeLevel = data[adapterPosition].volumeLevel + 10
                        }
                        changeVoiceUpdateView(
                                adapterPosition,
                                data[adapterPosition].volumeLevel,
                                data[adapterPosition].token
                        )
                    }

                    itemView.ivMinus.setOnClickListener {
                        if (data[adapterPosition].volumeLevel - 10 <= 0) {
                            data[adapterPosition].volumeLevel = 0
                        } else {
                            data[adapterPosition].volumeLevel = data[adapterPosition].volumeLevel - 10
                        }
                        changeVoiceUpdateView(
                                adapterPosition,
                                data[adapterPosition].volumeLevel,
                                data[adapterPosition].token
                        )
                    }
                }
            }
        }

        builder.rootView.rvUserList.layoutManager = GridLayoutManager(context, 2)
        builder.rootView.rvUserList.addItemDecoration(GridSpacingItemDecoration(2, SizeUtils.dp2px(15f), false))
        builder.rootView.rvUserList.adapter = adapter
        builder.rootView.ivConfirm.setOnClickListener {
            volumeMap.forEach {
                RoomApplication.getInstance()
                        .videoManager.setParameters("{\"che.audio.playout.uid.volume\": {\"uid\":${it.key},\"volume\":${it.value}}}")
            }

            val jsonObj = JSONObject(volumeMap)
            jsonObj.put(RoomConstant.ONE_TO_FOUR_ROOM_ID, roomId)
            SPUtils.getInstance().put(RoomConstant.SP_CLASSROOM_VOICE_SETTING, jsonObj.toString())
            dismiss()
        }

        builder.rootView.ivCancel.setOnClickListener {
            dismiss()
        }
    }

    private fun changeVoiceUpdateView(position: Int, volumeLevel: Int, token: String) {
        val itemView = builder.rootView.rvUserList.getChildAt(position)
        setVoiceLevel(itemView.ivVolume, volumeLevel)
        adapter?.getList()?.forEach {
            if (it.id != SPUtils.getInstance().getInt(RoomConstant.USER_ID)) {
                volumeMap[it.token] = it.volumeLevel
            }
        }
    }

    private fun setVoiceLevel(imageView: ImageView, volumeLevel: Int) {
        when {
            volumeLevel > 90 -> imageView.setImageDrawable(volumes!![10])
            volumeLevel > 80 -> imageView.setImageDrawable(volumes!![9])
            volumeLevel > 70 -> imageView.setImageDrawable(volumes!![8])
            volumeLevel > 60 -> imageView.setImageDrawable(volumes!![7])
            volumeLevel > 50 -> imageView.setImageDrawable(volumes!![6])
            volumeLevel > 40 -> imageView.setImageDrawable(volumes!![5])
            volumeLevel > 30 -> imageView.setImageDrawable(volumes!![4])
            volumeLevel > 20 -> imageView.setImageDrawable(volumes!![3])
            volumeLevel > 10 -> imageView.setImageDrawable(volumes!![2])
            volumeLevel > 0 -> imageView.setImageDrawable(volumes!![1])
            else -> imageView.setImageDrawable(volumes!![0])
        }
    }

    fun setVoiceJson(voiceJsonObj: com.alibaba.fastjson.JSONObject?) {
        this.voiceJsonObj = voiceJsonObj
    }

    fun setRoomID(roomId: Int) {
        this.roomId = roomId
    }

    fun getStudentCount(): Int {
        return studentList.size
    }

    fun setUserModel(students: MutableList<UserModel>) {
        setList(students)
        when {
            studentList.size == 0 -> {
                when {
                    isShowing() -> dismiss()
                }
                return
            }
            else -> {
                adapter?.setDataResource(studentList)
                adapter?.notifyDataSetChanged()
                when {
                    !isShowing() -> show()
                }
            }
        }
    }

    fun setList(students: MutableList<UserModel>) {
        var hasMySelf = false
        if (isShowing()) {
            students.forEach {
                if (it.id == SPUtils.getInstance().getInt(RoomConstant.USER_ID)) {
                    hasMySelf = true
                }
            }
        }

        if (hasMySelf && studentList.size == students.size + 1) {
            return
        }

        if (!hasMySelf && studentList.size == students.size) {
            return
        }

        studentList.clear()
        students.forEach {
            if (it.id != SPUtils.getInstance().getInt(RoomConstant.USER_ID)) {
                if (voiceJsonObj != null && voiceJsonObj?.contains(it.token)!!) {
                    val volumeLevel = ObjectUtil.getIntValue(voiceJsonObj?.getIntValue(it.token))
                    volumeMap[it.token] = volumeLevel
                    it.volumeLevel = volumeLevel
                } else {
                    volumeMap[it.token] = 100
                }
                studentList.add(it)
            }
        }
    }

    fun isShowing(): Boolean {
        return dialog.isShowing
    }

    fun setOnClickListener(clickListener: OnClickListener) {
        this.clickListener = clickListener
    }

    fun ImageView.loadImage(url: String) {
        val default = R.drawable.ic_avatar
        Glide.with(context).load(url).apply(RequestOptions().placeholder(default)).into(this)
    }
}
