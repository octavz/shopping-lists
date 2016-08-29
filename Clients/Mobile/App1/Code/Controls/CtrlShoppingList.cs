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
using App1.Code.Abstracts;
using App1.Extended.Code;

namespace App1.Code.Controls
{
    [Register("CtrlShoppingList")]
    public class CtrlShoppingList: AControl
    {
        private string m_ListName = null;

        /// <summary>
        /// CtrlShoppingList
        /// </summary>
        /// <param name="cnt"></param>
        public CtrlShoppingList(ShApplication cnt,string listName): base(cnt)
        {
            m_ListName = listName;

            Inflate(cnt, Resource.Layout.CtrlShoppingList, this);
            Initialize();
        }//CtrlShoppingList

        void Initialize()
        {            
            TextView lstNm = FindViewById<TextView>(Resource.Id.txtListName);
            lstNm.Text = m_ListName;
        }

        public string ListName   { get { return string.IsNullOrEmpty(m_ListName) ? string.Empty : m_ListName; } }
    }//CtrlShoppingList
}