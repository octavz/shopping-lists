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
            LoadLists();
            GenerateUILists();
        }


        /// <summary>
        /// GenerateUILists
        /// </summary>
        private void GenerateUILists()
        {
            ListsManager lstMgr = ListsManager.Instance;
            llShoppingLst.RemoveAllViews();
            lstMgr.Lists.ForEach(x =>
            {
                CreateUIList(x);
            });
        }//GenerateUILists

        private void LoadLists()
        {
            string data = FilesManager.ReadShListsState();
            if (string.IsNullOrEmpty(data))
                return;
            ListsManager lstMgr = ListsManager.Instance;
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
        private void CreateUIList(ShoppingListDTO newList)
        {            
            CtrlShoppingList item = new CtrlShoppingList(this, ShAppContext, newList);
            item.Event_DeleteItem += DeleteList;
            item.Event_EditItem += EditList;
            llShoppingLst.AddView(item, 0);
            llShoppingLst.RequestLayout();
        }//CreateUIList

        private void EditList(int listUIId, string newName)
        {
            var view = FindViewById<CtrlShoppingList>(listUIId);
            ListsManager.Instance.UpdateListName(view.Data.InternalId, newName);
        }//EditList

        /// <summary>
        /// DeleteList
        /// </summary>
        /// <param name="listUIId"></param>
        private void DeleteList(int listUIId)
        {
            var view = FindViewById<CtrlShoppingList>(listUIId);
            ((view as View).Parent as ViewGroup).RemoveView(view);
            ListsManager.Instance.DeleteList(view.Data.InternalId);
        }//DeleteList

        protected override void OnResume()
        {
            base.OnResume();
            GenerateUILists();
        }

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
