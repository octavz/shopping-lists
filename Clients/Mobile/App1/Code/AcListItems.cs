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
using ShList.Code.Abstracts;
using ShList.Code.Controls;
using CommonBL.Data;
using CommonBL.Managers;
using CommonBL.Utils;
using Android.Support.V4.Content;

namespace ShList.Code
{

    [Activity]
    public class AcListItems : APrivateActivity
    {
        ShoppingListDTO m_data = null;

        LinearLayout llLst = null;
        Button btnAddItem = null;
        EditText txtShoppingItem = null;
        EditText txtQuantity = null;

        protected override void OnCreate(Bundle bundle)
        {
            base.OnCreate(bundle);
            SetContentView(Resource.Layout.AcListItems);
            string id = Intent.GetStringExtra(Constants.KEY_ID_LIST);
            if (string.IsNullOrEmpty(id))
                return;
            else
            {
                m_data = ListsManager.Instance.GetListByInternalId(id);
                if (m_data == null)
                    return;
            }//else

            llLst = FindViewById<LinearLayout>(Resource.Id.lstItems);
            txtShoppingItem = FindViewById<EditText>(Resource.Id.txtItemName);
            txtQuantity = FindViewById<EditText>(Resource.Id.txtQuantity);
            btnAddItem = FindViewById<Button>(Resource.Id.btnAddItem);
            btnAddItem.Click += AddNewItem;

            GenerateUIListItems();

            IntentFilter filter = new IntentFilter(Intent.ActionSend);
            MessageReciever receiver = new MessageReciever(this);
            LocalBroadcastManager.GetInstance(this).RegisterReceiver(receiver, filter);
        }//OnCreate

        /// <summary>
        /// GenerateUILists
        /// </summary>
        private void GenerateUIListItems()
        {
            llLst.RemoveAllViews();
            for (int i = 0; i < m_data.Items.Count; i++)
            {
                CreateUIItem(m_data.Items[i], i);
            }
        }//GenerateUILists


        private void CreateUIItem(ItemListDTO newItem, int position = 0)
        {
            CtrlItemList item = new CtrlItemList(this, ShAppContext, newItem);
            //item.Event_DeleteItem += DeleteList;
            //item.Event_EditItem += EditList;
            //item.Event_ClickItem += ClickList;
            llLst.AddView(item, position);
            llLst.RequestLayout();
        }//CreateUIList


        private void AddNewItem(object sender, EventArgs e)
        {
            int quantity = Convert.ToInt32(txtQuantity.Text);
            ItemListDTO newItemDto = ListsManager.Instance.CreateListItem(m_data.InternalId, txtShoppingItem.Text, quantity);
            CreateUIItem(newItemDto, 0);
        }//AddNewList


        public void ProcessMessage(Intent intent)
        {
            //intent.GetStringExtra("WearMessage");        
            ShoppingListDTO wantedList = ListsManager.Instance.Lists.Where(x => x.InternalId == m_data.InternalId).FirstOrDefault(); //check if the list was not deleted
            if (wantedList == null)
            {
                StartActivity(new Intent(this, typeof(AcShoppingLists)));
                return;
            }//endif

            var allUiLists = wantedList.Items.Where(x => x.IsDeleted == false).OrderBy(x => x.Date).ToList();

            ViewGroup viewGroup = (ViewGroup)llLst;
            //remove all that are not in the datalist
            for (int i = 0; i < viewGroup.ChildCount; i++)
            {
                CtrlItemList child = viewGroup.GetChildAt(i) as CtrlItemList;
                if (child == null) continue;
                var foundItem = allUiLists.Where(x => x.InternalId == child.Data.InternalId).FirstOrDefault();
                if (foundItem == null)
                    ((child as View).Parent as ViewGroup).RemoveView(child);
            }//for

            for (int i = 0; i < allUiLists.Count; i++)
            {
                CtrlItemList wantedView = null;
                for (int j = 0; j < viewGroup.ChildCount; j++)
                {
                    CtrlItemList child = viewGroup.GetChildAt(j) as CtrlItemList;
                    if (child.Data.InternalId == allUiLists[i].InternalId)
                    {
                        wantedView = child;
                        break;
                    }
                }//for

                if (wantedView != null)
                {
                    // update the existing ones
                    wantedView.UpdateCtrlData(allUiLists[i]);
                }
                else
                {
                    CreateUIItem(allUiLists[i], i);
                }
            }//for

        }//ProcessMessage

        internal class MessageReciever : BroadcastReceiver
        {
            AcListItems _main;
            public MessageReciever(AcListItems owner) { this._main = owner; }
            public override void OnReceive(Context context, Intent intent)
            {
                _main.ProcessMessage(intent);
            }
        }
    }
}