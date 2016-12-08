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
using CommonBL.Data.Request;
using CommonBL.Repository;
using CommonBL.Data.Response;
using System.Threading.Tasks;
using ShList.Code.Common;
using Android.Support.V4.Content;
using CommonBL.Utils;

namespace ShList.Code
{

    [Activity(Label = "@string/ActivityShoppingList")]
    public class AcShoppingLists : APrivateActivity
    {
        LinearLayout llShoppingLst = null;
        Button btnCreateList = null;
        SyncCom con = null;

        protected override void OnCreate(Bundle bundle)
        {
            base.OnCreate(bundle);

            // Set our view from the "main" layout resource
            SetContentView(Resource.Layout.AcShoppingLists);

            var startServiceIntent = new Intent(ShAppContext, typeof(SyncService));
            con = new SyncCom(this);
            ApplicationContext.BindService(startServiceIntent, con, Bind.AutoCreate);

            llShoppingLst = FindViewById<LinearLayout>(Resource.Id.listShopping);
            btnCreateList = FindViewById<Button>(Resource.Id.btnCreateList);

            btnCreateList.Click += AddNewList;

            LoadLists();
            GenerateUILists();

            IntentFilter filter = new IntentFilter(Intent.ActionSend);
            MessageReciever receiver = new MessageReciever(this);
            LocalBroadcastManager.GetInstance(this).RegisterReceiver(receiver, filter);
        }//OnCreate

        public void ProcessMessage(Intent intent)
        {
            //intent.GetStringExtra("WearMessage");
            int a = 2;

            var allUiLists = ListsManager.Instance.Lists.Where(x => x.IsDeleted == false).OrderBy(x => x.ListDate).ToList();


            ViewGroup viewGroup = (ViewGroup)llShoppingLst;
            //remove all that are not in the datalist
            for (int i = 0; i < viewGroup.ChildCount; i++)
            {
                CtrlShoppingList child = viewGroup.GetChildAt(i) as CtrlShoppingList;
                if (child == null) continue;
                var foundList = allUiLists.Where(x => x.InternalId == child.Data.InternalId).FirstOrDefault();
                if (foundList == null)
                    ((child as View).Parent as ViewGroup).RemoveView(child);
            }//for

            for (int i = 0; i < allUiLists.Count; i++)
            {
                CtrlShoppingList wantedView = null;
                for (int j = 0; j < viewGroup.ChildCount; j++)
                {
                    CtrlShoppingList child = viewGroup.GetChildAt(j) as CtrlShoppingList;
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
                    CreateUIList(allUiLists[i], i);
                }
            }//for

        }//ProcessMessage

        /// <summary>
        /// GenerateUILists
        /// </summary>
        private void GenerateUILists()
        {
            ListsManager lstMgr = ListsManager.Instance;
            for (int i = 0; i < lstMgr.Lists.Count; i++)
            {
                CreateUIList(lstMgr.Lists[i], i);
            }
        }//GenerateUILists

        private void LoadLists()
        {
            ListsManager lstMgr = ListsManager.Instance;
            string data = string.Empty;
            if (lstMgr.Lists.Count == 0)
                data = FilesManager.ReadShListsState();
            if (string.IsNullOrEmpty(data))
                return;

            lstMgr.ImportSerializedDataFromLocalStorage(data);
        }//LoadLists

        private void AddNewList(object sender, EventArgs e)
        {
            ShoppingListDTO newList = ListsManager.Instance.CreateNewList();
            CreateUIList(newList);
        }//AddNewList

        /// <summary>
        /// CreateUIList
        /// </summary>
        /// <param name="newList"></param>
        /// <param name="progressDialog"></param>
        private void CreateUIList(ShoppingListDTO newList, int position = 0)
        {
            CtrlShoppingList item = new CtrlShoppingList(this, ShAppContext, newList);
            item.Event_DeleteItem += DeleteList;
            item.Event_EditItem += EditList;
            item.Event_ClickItem += ClickList;
            llShoppingLst.AddView(item, position);
            llShoppingLst.RequestLayout();
        }//CreateUIList

        /// <summary>
        /// ClickList
        /// </summary>
        /// <param name="listUIId"></param>
        private void ClickList(int listUIId)
        {
            var view = FindViewById<CtrlShoppingList>(listUIId);
            if (view == null)
                return;

            var intItems = new Intent(this, typeof(AcListItems));
            intItems.PutExtra(Constants.KEY_ID_LIST, view.Data.InternalId);
            StartActivity(intItems);
        }//ClickList

        private void EditList(int listUIId, string newName)
        {
            var view = FindViewById<CtrlShoppingList>(listUIId);
            if (view != null)
            {
                ListsManager.Instance.UpdateListName(view.Data.InternalId, newName);
            }
        }//EditList

        /// <summary>
        /// DeleteList
        /// </summary>
        /// <param name="listUIId"></param>
        private void DeleteList(int listUIId)
        {
            var view = FindViewById<CtrlShoppingList>(listUIId);
            if (view != null)
            {
                ((view as View).Parent as ViewGroup).RemoveView(view);
                ListsManager.Instance.DeleteList(view.Data.InternalId);
            }//endif
        }//DeleteList

        protected override void OnResume()
        {
            base.OnResume();
            llShoppingLst.RemoveAllViews();
            GenerateUILists();
        }//OnResume

        internal class MessageReciever : BroadcastReceiver
        {
            AcShoppingLists _main;
            public MessageReciever(AcShoppingLists owner) { this._main = owner; }
            public override void OnReceive(Context context, Intent intent)
            {
                _main.ProcessMessage(intent);
            }
        }
    }
}
