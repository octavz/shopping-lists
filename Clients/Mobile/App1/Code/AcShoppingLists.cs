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

namespace ShList.Code
{

    [Activity(Label = "@string/ActivityShoppingList")]
    public class AcShoppingLists : APrivateActivity
    {
        LinearLayout llShoppingLst = null;
        Button btnCreateList = null;

        protected async override void OnCreate(Bundle bundle)
        {
            base.OnCreate(bundle);

            // Set our view from the "main" layout resource
            SetContentView(Resource.Layout.AcShoppingLists);
            
            llShoppingLst = FindViewById<LinearLayout>(Resource.Id.listShopping);
            btnCreateList = FindViewById<Button>(Resource.Id.btnCreateList);

            btnCreateList.Click += AddNewList;

            await LoadLists();
            GenerateUILists();
        }//OnCreate

        /// <summary>
        /// GenerateUILists
        /// </summary>
        private void GenerateUILists()
        {
            ListsManager lstMgr = ListsManager.Instance;
            lstMgr.Lists.ForEach(x => {
                CreateUIList(x);
            });
        }//GenerateUILists

        private async Task LoadLists()
        {
            var progressDialog = ProgressDialog.Show(this, ShAppContext.GetString(Resource.String.PleaseWait), ShAppContext.GetString(Resource.String.LoadingLists), true);
            ResUserLists resLists = await UserRepository.Instance.GetUserLists(ShAppContext.UserId, ShAppContext.UserToken);
            ListsManager lstMgr = ListsManager.Instance;
            resLists.LstItems.ForEach(x => lstMgr.AddListFromResponse(x));
            progressDialog.Dismiss();
            return;
        }//LoadLists

        private async void AddNewList(object sender, EventArgs e)
        {
            ShoppingListDTO newList = ListsManager.Instance.CreateNewList();
            ReqListDTO lst = newList.GenerateRequestFormat(ShAppContext.UserId, ShAppContext.UserToken);

            var progressDialog = ProgressDialog.Show(this, ShAppContext.GetString(Resource.String.PleaseWait), ShAppContext.GetString(Resource.String.CreatingList), true);
            ResListDTO resList = await ListRepository.Instance.CreateList(lst);
            CreateUIList(newList);
            progressDialog.Dismiss();
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
            llShoppingLst.AddView(item, 0);
        }//CreateUIList

        /// <summary>
        /// DeleteList
        /// </summary>
        /// <param name="listUIId"></param>
        private async Task DeleteList(int listUIId)
        {
            var view = FindViewById<CtrlShoppingList>(listUIId);
            ((view as View).Parent as ViewGroup).RemoveView(view);
            ListsManager.Instance.DeleteList(view.Data);

            if (!string.IsNullOrEmpty(view.Data.Id)) //the list was not sync with the server
            {
                var progressDialog = ProgressDialog.Show(this, ShAppContext.GetString(Resource.String.PleaseWait), ShAppContext.GetString(Resource.String.DeletingList), true);
                ResDeleteListDTO resDelete = await ListRepository.Instance.DeleteList(view.Data.Id, ShAppContext.UserToken);
                progressDialog.Dismiss();
            }//endif
        }//DeleteList
    }
}
