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
using ShList.Extended.Code;

namespace ShList.Code.Abstracts
{
    public abstract class AControl : LinearLayout
    {
        private ShApplication m_Context;

        //AControl
        public AControl(ShApplication context): base(context)
        {            
            m_Context = context;
        }//AControl

        /// <summary>
        /// Shopping list - Application Cotext
        /// </summary>
        public ShApplication ShAppContext { get { return m_Context; } }
    }
}