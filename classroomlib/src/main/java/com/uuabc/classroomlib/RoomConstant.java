package com.uuabc.classroomlib;

/**
 * RoomConstant
 * Created by wb on 2018/3/14.
 */

public class RoomConstant {
    public static final String ApiVersion = "4.0.2";

    /* Actions */
    public static final String USER_ID = "userId";
    public static final String USER_CHILD_ID = "userChildId";
    public static final String TOKEN = "token";
    public static final String USER_NAME = "userName";
    public static final String ENGLISH_NAME = "englishName";
    public static final String USER_AVATAR = "userAvatar";

    /*result status*/
    public static final int RESULT_OK_STATUS = 1;
    public static final int RESULT_OK_CODE = 200;
    public static final int RESULT_CLASS_OVER_CODE = 0;

    public static final int RESULT_CODE_LEARNING = 301;
    public static final int RESULT_CODE_INVALID = 999;

    public static final int STUDENT_TYPE = 1;
    public static final int TEACHER_TYPE = 2;
    public static final String TEXT_END_TYPE = "textend";
    public static final String TEXT_START_TYPE = "textstart";

    public static final String SYSTEM = "Android";

    public static final String SP_CLASSROOM_VOICE_SETTING = "spClassRoomVoiceSetting";
    public static final String JOIN_AGORA_ERROR = "joinAgoraError";
    public static final String ONE_TO_FOUR_ROOM_ID = "oneToFourRoomId";

    /*************监听事件***************/
    public static final String AUTHENTICATED = "authenticated";//身份验证成功
    public static final String OFF_LINE = "offline";//服务器端通知断开
    public static final String USER_ENTER = "userEnter";//新的用户加入
    public static final String USER_QUIT = "userQuit";//用户退出
    public static final String ENTER_SUCCESS = "enterSuccess";//登入成功
    public static final String ENTER_REJECT = "enterReject";//登入拒绝
    public static final String LIVE_COUNTER = "liveCounter";//在线人数
    public static final String GROUP = "group";//分组
    public static final String REFRESH_CLASSROOM = "refresh";//刷新教室
    public static final String CLASSROOM_ENTRY_CHANGED = "classroomEntryChanged";
    public static final String OUT_ROOM_EVENT = "outRoomEvent";
    public static final String SPEAKING = "speaking";
    /**************share-共享事件******************/
    public static final String FLOWER = "flower";//奖励钻石后更新的钻石列表
    public static final String MUTED = "muted"; //对学生禁言
    public static final String DRAW = "draw";//涂鸦开关
    public static final String DRAWING = "drawing";//涂鸦开关
    public static final String ANIMATE = "animate";//互动权限
    public static final String TEXTSTART = "textstart";//课件打字| 老师端操作
    public static final String DRAW_TEXT = "draw_text";
    public static final String EXCIT = "excit";//激励动画
    public static final String ROSTRUM = "rostrum";//控制学生上下台 | 老师端操作
    public static final String POSITION = "position";//鼠标事件
    public static final String LINE = "line";//涂鸦画线
    public static final String CHAT = "chat";//聊天
    public static final String PAGE = "page";//翻页
    public static final String CLEAR = "cls"; //清除课件区域屏幕
    public static final String COURSEWARE = "courseware";//切换课件
    public static final String TOPIC = "topic"; //直播课答题相关
    public static final String SUBMIT = "submit"; //直播课提交答案
    public static final String STUDENTS = "students"; //学生列表信息
    /**************发送事件******************/
    public static final String LOGIN = "login";//登入
    public static final String AUTHENTICATE = "authenticate";//身份验证
    public static final String JOIN = "join";//加入房间
    public static final String LEAVE_ROOM = "leaveRoom";//离开房间
    public static final String TV_LOGOUT = "TVLogout";//电视退出账号
    public static final String DEVICE_BIND = "device_bind";
    /**************发送,监听公共事件******************/
    public static final String ACTION = "action";//交互课件中的交互指令
    public static final String COMPLETE = "complete";//下课通知
    public static final String SHARE = "share";//共享事件
    /*****************type-画线****************/
    public static final String START = "start";
    public static final String MOVE = "move";
    public static final String END = "end";
    /*****************TOPIC***************/
    public static final String RESULT = "result";
    public static final String FINISHED = "finished";
    public static final String CLOSED = "closed";
    public static final String CENTER = "center";
    public static final String STAT = "stat";
    public static final String PUBINFO = "pubinfo";
    /******************pc********************/
    public static final float PC_WIDTH = 1040;
    public static final float PC_HEIGHT = 780;
    public static final float PC_ULIVE_WIDTH = 800;
    public static final float PC_ULIVE_HEIGHT = 600;
    /****************COLOR******************/
    public static final String COLOR_RED = "#ff3b2f";
    public static final String COLOR_YELLOW = "#f7d158";
    public static final String COLOR_BLUE = "#1b8bfe";
    public static final String COLOR_GREEN = "#2dc21b";
    public static final String COLOR_ORANGE = "#ff8000";
    public static final String COLOR_PURPLE = "#7c76f7";
    /********************运行状态*********************/
    public static final String IS_FOREGROUND = "isForeground";
    public static final String FORM_NOTIFICATION = "formNotification";
    /********************课程类型*********************/
    public static final int CLASS_TYPE_ONE_TO_ONE = 1;//一对一
    public static final int CLASS_TYPE_ONE_TO_FOUR = 2;//一对四
    public static final int CLASS_TYPE_LIVE = 3;//直播
    public static final int CLASS_TYPE_COURT = 4;//包场
    /**********************缓存文件夹****************/
    public static final String CACHE = "uuabc";
    /**********************拓课云|声网****************/
    public static final String ROOM_JOINED = "roomJoined";
    public static final String ROOM_ERROR = "roomError";
    public static final String ROOM_USER_JOINED = "roomUserJoined";
    public static final String ROOM_USER_LEAVED = "roomUserLeaved";
    public static final String ROOM_KICKED_OUT = "roomKickedOut";
    public static final String ROOM_LEAVE_SUCCESS = "roomLiveSuccess";
    public static final String ROOM_VOLUME = "roomVolume";
    public static final String ROOM_NET = "roomNet";
    /**********************友盟统计---教室****************/
    public static final String ONE_TO_ONE_TO_CLASS = "1To1ToClass";//1v1进入教室次数
    public static final String ONE_TO_ONE_CLASS_OVER = "1To1ClassOver";//1v1下课次数
    public static final String ONE_TO_ONE_QUIT_CLASS = "1To1QuitClass"; //1v1弹窗提示是否退出教室次数
    public static final String ONE_TO_ONE_MAKE_SURE_QUIT_CLASS = "1To1MakeSureQuitClass"; //1v1退出教室次数
    public static final String ONE_TO_ONE_CLICK_REFRESH = "1To1ClickRefresh";//1v1刷新教室次数
    public static final String ONE_TO_ONE_RECONNECT = "1To1Reconnect";//1v1 Socket断线重连次数
    public static final String ONE_TO_ONE_ERROR = "1To1Error";//1v1报错次数(网络断线)
    public static final String ONE_TO_ONE_CLICK_PEN_COLOR = "1To1ClickPenColor";//1v1画笔的每个颜色选择次数
    public static final String ONE_TO_ONE_CLICK_PEN_SIZE = "1To1ClickPenSize";//1v1画笔粗细选择次数
    public static final String ONE_TO_FOUR_TO_CLASS = "1To4ToClass";//1v4进入教室次数
    public static final String ONE_TO_FOUR_CLASS_OVER = "1To4ClassOver";//1v4下课次数
    public static final String ONE_TO_FOUR_QUIT_CLASS = "1To4QuitClass";//1v4弹窗提示是否退出教室次数
    public static final String ONE_TO_FOUR_MAKE_SURE_QUIT_CLASS = "1To4MakeSureQuitClass";//1v4退出教室次数
    public static final String ONE_TO_FOUR_CLICK_REFRESH = "1To4ClickRefresh";//1v4刷新教室次数
    public static final String ONE_TO_FOUR_RECONNECT = "1To4Reconnect";//1v4 Socket断线重连次数
    public static final String ONE_TO_FOUR_ERROR = "1To4Error";//1v4报错次数(网络断线)
    public static final String ONE_TO_FOUR_CLICK_PEN_COLOR = "1To4ClickPenColor";//1v4画笔的每个颜色选择次数
    public static final String ONE_TO_FOUR_CLICK_PEN_SIZE = "1To4ClickPenSize";//1v4画笔粗细选择次数
    public static final String LIVE_QUIT_CLASS = "LiveQuitClass";
    public static final String LIVE_MAKE_SURE_QUIT_CLASS = "LiveMakeSureQuitClass";
    public static final String LIVE_CLICK_REFRESH = "LiveClickRefresh";
    public static final String LIVE_RECONNECT = "LiveReconnect";
    public static final String LIVE_ERROR = "LiveError";
    public static final String LIVE_CLICK_PEN_COLOR = "LiveClickPenColor";
    public static final String LIVE_CLICK_PEN_SIZE = "LiveClickPenSize";
    public static final String LIVE_CLICK_CLEAN = "LiveClickClean";
    public static final String LIVE_SEND_STARS = "LiveSendStars";
    /**********************教室的提示框********************/
    public static final int EXIT = 0;
    public static final int REFRESH = 1;
    public static final int OFFLINE = 2;
    /*************************JPush***********************/
    public static final String JPUSH_ID = "JPush_Id";
    /*************************网络连接***********************/
    public static final String NET_WORK_CONNECTED = "netWorkConnected";
    public static final String NET_WORK_INCONNECTED = "netWorkInConnected";
    /************************WIFI强度***********************/
    public static final int WIFI_LEVEL_STRONG = 1;
    public static final int WIFI_LEVEL_MID = 2;
    public static final int WIFI_LEVEL_WEEK = 3;
    public static final int WIFI_LEVEL_NULL = 0;
    /************************SP_HOST_KEY***********************/
    public static final String ONLINE_CLASS_HOST = "onLineClassHost";
    public static final String ONLINE_SS_COURSE_HOST = "onLineSSCourseHost";
    public static final String GRAPHQL_URL = "graphqlUrl";
    /************************数据录制，保存上课过程中各项数据***********************/
    public static final int RECORD_ENTER = 1;//进入
    public static final int RECORD_EXIT = 2;//退出
    public static final int RECORD_VIDEO_PUB = 13;//视频开始
    public static final int RECORD_VIDEO_STOP = 14;//视频结束
    public static final int RECORD_POINT = 17;//划线
    public static final int RECORD_ANIMATE_INFO = 19;//交互动画
    /************************声网log缓存路径***********************/
    public static final String AGORA_LOG_S_ONE_TO_ONE = "/agoralog/stu_s_1v1_agoraLog.txt";
    public static final String AGORA_LOG_S_ONE_TO_FOUR = "/agoralog/stu_s_1v4_agoraLog.txt";
    public static final String AGORA_LOG_S_LIVE = "/agoralog/stu_s_live_agoraLog.txt";
    public static final String AGORA_LOG_U_LIVE = "/agoralog/stu_u_live_agoraLog.txt";
    /************************lottie动画缓存路径***********************/
    public static final String LOTTIE_FACIAL_PATH = "/lottie/";
    /************************课件中录音文件缓存路径***********************/
    public static final String RECORD_AUDIO_PATH = "/audioRecord/";
    /************************互动课件相关***********************/
    public static final String SP_LOADED_REPLY = "spLoadedReply";//互动课件是否已经加载过引导课件
    public static final String LOAD_REPLY_URL = "https://sit-classroom.uuabc.com/loading.html";//非桥接文件直接加载引导文件

    public static final String ENTER_DESTORY_AROTE_VIEW = "eventFinishAgoreView";

    public static final String SP_EMOTICON_UPDATED_TIME = "spEmoticonUpdatedTime";
}
