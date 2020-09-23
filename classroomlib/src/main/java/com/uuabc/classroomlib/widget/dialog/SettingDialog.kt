package com.uuabc.classroomlib.widget.dialog

import android.content.Context
import android.graphics.drawable.Drawable
import android.view.LayoutInflater
import android.view.ViewGroup
import android.widget.ImageView
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.blankj.utilcode.util.SPUtils
import com.blankj.utilcode.util.SizeUtils
import com.bumptech.glide.Glide
import com.bumptech.glide.request.RequestOptions
import com.uuabc.classroomlib.R
import com.uuabc.classroomlib.RoomApplication
import com.uuabc.classroomlib.RoomConstant
import com.uuabc.classroomlib.model.SocketModel.UserModel
import com.uuabc.classroomlib.utils.CompatUtil
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

    private var voiceJsonObj: com.alibaba.fastjson.JSONObject? = null
    var adapter: AlphaAdapter<UserModel, AlphaAdapter.Holder>? = null
    var volumeMap = HashMap<String, Int>()
    private var studentList = ArrayList<UserModel>()
    private var volumes: Array<Drawable>? = null
    private var roomId = 0

    init {
        this.context = context

        volumes = arrayOf(
                CompatUtil.getDrawable(context, R.drawable.ic_setting_volume_level_0),
                CompatUtil.getDrawable(context, R.drawable.ic_setting_volume_level_1),
                CompatUtil.getDrawable(context, R.drawable.ic_setting_volume_level_2),
                CompatUtil.getDrawable(context, R.drawable.ic_setting_volume_level_3),
                CompatUtil.getDrawable(context, R.drawable.ic_setting_volume_level_4),
                CompatUtil.getDrawable(context, R.drawable.ic_setting_volume_level_5),
                CompatUtil.getDrawable(context, R.drawable.ic_setting_volume_level_6),
                CompatUtil.getDrawable(context, R.drawable.ic_setting_volume_level_7),
                CompatUtil.getDrawable(context, R.drawable.ic_setting_volume_level_8),
                CompatUtil.getDrawable(context, R.drawable.ic_setting_volume_level_9),
                CompatUtil.getDrawable(context, R.drawable.ic_setting_volume_level_10)
        )

        builder = CaptainDialog.Builder(context)
        val dialogHeight = RoomApplication.getInstance().getScreenHeight() * 0.7
        val dialogWidth = dialogHeight * 1.365
        dialog = builder.cancelTouchout(false)
                .view(R.layout.dialog_room_sdk_setting)
                .widthpx(dialogWidth.toInt())
                .heightpx(dialogHeight.toInt())
                .style(R.style.Dialog_No_Title)
                .build()
        dialog.window?.setBackgroundDrawableResource(R.color.transparent)
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
                        if (adapterPosition == RecyclerView.NO_POSITION) return@setOnClickListener
                        if (data[adapterPosition].volumeLevel + 10 >= 100) {
                            data[adapterPosition].volumeLevel = 100
                        } else {
                            data[adapterPosition].volumeLevel = data[adapterPosition].volumeLevel + 10
                        }
                        changeVoiceUpdateView(
                                adapterPosition,
                                data[adapterPosition].volumeLevel
                        )
                    }

                    itemView.ivMinus.setOnClickListener {
                        if (adapterPosition == RecyclerView.NO_POSITION) return@setOnClickListener
                        if (data[adapterPosition].volumeLevel - 10 <= 0) {
                            data[adapterPosition].volumeLevel = 0
                        } else {
                            data[adapterPosition].volumeLevel = data[adapterPosition].volumeLevel - 10
                        }
                        changeVoiceUpdateView(
                                adapterPosition,
                                data[adapterPosition].volumeLevel
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

    private fun changeVoiceUpdateView(position: Int, volumeLevel: Int) {
        val itemView = builder.rootView.rvUserList.getChildAt(position)
        setVoiceLevel(itemView.ivVolume, volumeLevel)
        adapter?.getList()?.forEach {
            if (it.id != SPUtils.getInstance().getInt(RoomConstant.USER_ID)) {
                volumeMap[it.token] = it.volumeLevel
            }
        }
    }

    private fun setVoiceLevel(imageView: ImageView, volumeLevel: Int) {
        volumes?.let {
            when {
                volumeLevel > 90 -> imageView.setImageDrawable(it[10])
                volumeLevel > 80 -> imageView.setImageDrawable(it[9])
                volumeLevel > 70 -> imageView.setImageDrawable(it[8])
                volumeLevel > 60 -> imageView.setImageDrawable(it[7])
                volumeLevel > 50 -> imageView.setImageDrawable(it[6])
                volumeLevel > 40 -> imageView.setImageDrawable(it[5])
                volumeLevel > 30 -> imageView.setImageDrawable(it[4])
                volumeLevel > 20 -> imageView.setImageDrawable(it[3])
                volumeLevel > 10 -> imageView.setImageDrawable(it[2])
                volumeLevel > 0 -> imageView.setImageDrawable(it[1])
                else -> imageView.setImageDrawable(it[0])
            }
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
        when (studentList.size) {
            0 -> {
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

    fun ImageView.loadImage(url: String) {
        val default = R.drawable.ic_avatar
        Glide.with(context).load(url).apply(RequestOptions().placeholder(default)).into(this)
    }
}
