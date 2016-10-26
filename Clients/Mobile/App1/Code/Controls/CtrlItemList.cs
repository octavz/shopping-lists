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
using CommonBL.Data;
using ShList.Extended.Code;
using ShList.Code.Abstracts;

namespace ShList.Code.Controls
{
    [Register("CtrlItemList")]
    public class CtrlItemList : AControl
    {

        private Activity m_ParentActivity = null;


        public event Action<int> Event_DeleteItem = null;

        /// <summary>
        /// CtrlShoppingList
        /// </summary>
        /// <param name="cnt"></param>
        public CtrlItemList(Activity parent, ShApplication cnt, ItemListDTO data) : base(cnt)
        {
            this.Id = View.GenerateViewId();
            m_ParentActivity = parent;
            Inflate(cnt, Resource.Layout.CtrlItemList, this);
            Initialize();
        }//CtrlShoppingList

        void Initialize()
        {


        }
    }
}