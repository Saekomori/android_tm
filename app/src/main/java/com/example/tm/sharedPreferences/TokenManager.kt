import android.content.Context
import android.content.SharedPreferences

    object TokenManager {
        private lateinit var sharedPreferences: SharedPreferences
        private const val PREF_NAME = "MyPrefs"
        private const val KEY_TOKEN = "token"


        private const val INVITE_TOKEN = "tokenInvite"

        fun init(context: Context) {
            sharedPreferences = context.getSharedPreferences(PREF_NAME, Context.MODE_PRIVATE)
        }

        var token: String?
            get() = sharedPreferences.getString(KEY_TOKEN, null)
            set(value) {
                sharedPreferences.edit().putString(KEY_TOKEN, value).apply()
            }

//        var tokenInvite: String?
//            get() = sharedPreferences.getString(INVITE_TOKEN, null)
//            set(value) {
//                sharedPreferences.edit().putString(INVITE_TOKEN, value).apply()
//            }

        fun clearToken() {
            sharedPreferences.edit().remove(KEY_TOKEN).apply()
        }
//        fun clearTokenInvite() {
//            sharedPreferences.edit().remove(INVITE_TOKEN).apply()
//        }
    }

