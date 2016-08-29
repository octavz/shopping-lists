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

namespace App1.Code.Abstracts
{
    public abstract class AControl : LinearLayout
    {
        private Context m_Context;

        //AControl
        public AControl(Context context): base(context)
        {            
            m_Context = context;
        }//AControl

    }
}