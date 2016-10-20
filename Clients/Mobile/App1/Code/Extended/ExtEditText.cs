using System;
using System.Collections.Generic;
using System.Linq;
using System.Text;

using Android.App;
using Android.Content;
using Android.OS;
using Android.Runtime;
using Android.Views;
using Android.Widget;
using Android.Support.V4.Content;

namespace ShList.Code.Extended
{
    public static class ExtEditText
    {

        /// <summary>
        /// ShowError
        /// </summary>
        /// <param name="txtEdit"></param>
        /// <param name="sErrorMsg"></param>
        public static void ShowError(this Android.Widget.EditText txtEdit, string sErrorMsg, Context cnt)
        {
            Android.Graphics.Drawables.Drawable icon = ContextCompat.GetDrawable(cnt, Resource.Drawable.val_error);
            txtEdit.SetError(sErrorMsg, null);
            txtEdit.SetCompoundDrawablesWithIntrinsicBounds(0, 0, Resource.Drawable.val_error, 0);
        }//ShowError

        public static void HideError(this Android.Widget.EditText txtEdit)
        {
            txtEdit.SetError((string)null, null);
            txtEdit.SetCompoundDrawablesWithIntrinsicBounds(0, 0, 0, 0);
        }//HideError
    }
}