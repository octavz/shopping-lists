using CommonBL.Data;
using CommonBL.Data.Request;
using CommonBL.Data.Response;
using CommonBL.Utils;
using Newtonsoft.Json;
using System;
using System.Collections.Generic;
using System.Linq;
using System.Net.Http;
using System.Text;
using System.Threading;
using System.Threading.Tasks;

namespace CommonBL.Managers
{
    public class ListsManager
    {
        private static readonly Lazy<ListsManager> m_Manager = new Lazy<ListsManager>(() => new ListsManager());
        private object mLocker = new object();
        private ShStorage mStorage = new ShStorage();

        /// <summary>
        /// ListsManager - constructor
        /// </summary>
        private ListsManager()
        {
        }//ListsManager

        public ShoppingListDTO CreateNewList()
        {
            lock (mLocker)
            {
                ShoppingListDTO newList = new ShoppingListDTO()
                {
                    ListDate = DateTime.Now,
                    IsDirty = true,
                    InternalId = Guid.NewGuid().ToString()
                };
                mStorage.ShLists.Add(newList);
                return newList;
            }//lock           
        }//AddNewList

        /// <summary>
        /// CreateListItem
        /// </summary>
        /// <param name="listInternalId"></param>
        /// <param name="description"></param>
        /// <param name="quantity"></param>
        /// <returns></returns>
        public ItemListDTO CreateListItem(string listInternalId, string description, int quantity)
        {
            ShoppingListDTO lst = GetListByInternalId(listInternalId);
            if (lst == null)
                return null;

            lock (mLocker)
            {
                ItemListDTO item = new ItemListDTO();
                item.InternalId = Guid.NewGuid().ToString();
                item.Quantity = quantity;
                item.Description = description;

                lst.Items.Add(item);
                lst.IsDirty = true;
                return item;
            }
        }//CreateListItem


        public void UpdateListName(string UIId, string newName)
        {
            var aList = mStorage.ShLists.Where(x => x.InternalId == UIId).FirstOrDefault();
            if (aList == null)
                return;
            lock (mLocker)
            {
                aList.ListName = newName;
                aList.IsDirty = true;
            }
        }//UpdateListName

        public void DeleteList(string UIId)
        {
            var aList = mStorage.ShLists.Where(x => x.InternalId == UIId).FirstOrDefault();
            if (aList == null)
                return;
            lock (mLocker)
            {
                aList.IsDeleted = true;
                aList.IsDirty = true;
                aList.InternalId = null;
            }
        }//DeleteList

        /// <summary>
        /// DeleteListItem
        /// </summary>
        /// <param name="sListUIId"></param>
        /// <param name="sElemUIId"></param>
        public void DeleteListItem(string sListUIId,string sElemUIId)
        {
            var aList = mStorage.ShLists.Where(x => x.InternalId == sListUIId).FirstOrDefault();
            if (aList == null)
                return;
            var wantedItem = aList.Items.Where(x => x.InternalId == sElemUIId).FirstOrDefault();
            if (wantedItem == null)
                return;            

            lock (mLocker)
            {
                wantedItem.InternalId = null;
                aList.Items.Remove(wantedItem);                
                aList.IsDirty = true;                
            }
        }//DeleteListItem

        public void ItemBought(string sListUIId, string sElemUIId, bool bought)
        {
            var aList = mStorage.ShLists.Where(x => x.InternalId == sListUIId).FirstOrDefault();
            if (aList == null)
                return;
            var wantedItem = aList.Items.Where(x => x.InternalId == sElemUIId).FirstOrDefault();
            if (wantedItem == null)
                return;

            lock (mLocker)
            {
                wantedItem.Bought = bought;                
                aList.IsDirty = true;
            }
        }//ItemBought

        /// <summary>
        /// GetListByInternalId
        /// </summary>
        /// <param name="id"></param>
        /// <returns></returns>
        public ShoppingListDTO GetListByInternalId(string id)
        {
            var aList = mStorage.ShLists.Where(x => x.InternalId == id).FirstOrDefault();
            return aList;
        }//GetListByInternalId


        public string GetSerializedDataForLocalStorage()
        {
            var json = JsonConvert.SerializeObject(mStorage);
            // var content = new StringContent(json, Encoding.UTF8, Constants.MEDIA_TYPE);
            return json;
        }//GetSerializedDataForLocalStorage

        public void ImportSerializedDataFromLocalStorage(string json)
        {
            try
            {
                mStorage = JsonConvert.DeserializeObject<ShStorage>(json);
            }
            catch (Exception e)
            {
                return;
            }
        }//ImportSerializedDataFromLocalStorage


        public ReqSyncDTO GenerateRequestDTOForSync(string userId, string loginAcc)
        {
            ReqSyncDTO aReq = new ReqSyncDTO();
            aReq.userData.id = userId;
            aReq.userData.login = loginAcc;

            mStorage.ShLists.ForEach(L =>
            {
                ShListDTO aList = new ShListDTO();
                aList.created = (long)Tools.TimeStampToUnixDateTime(L.ListDate);
                aList.description = L.ListDescription;
                aList.name = string.IsNullOrEmpty(L.ListName) ? Constants.DEFAULT_LIST_NAME : L.ListName;
                aList.userId = userId;
                aList.id = string.IsNullOrEmpty(L.Id) ? null : L.Id;
                aList.status = L.IsDeleted ? 5 : 0;
                aList.clientTag = L.InternalId;

                aList.items = L.Items.Count > 0 ? new List<ItemDTO>() : null;

                L.Items.ForEach(I =>
                {
                    ItemDTO anItem = new ItemDTO();
                    anItem.productId = string.IsNullOrEmpty(I.ProductId) ? null : I.ProductId;
                    anItem.quantity = I.Quantity;
                    anItem.description = I.Description;
                    anItem.status = I.IsDeleted ? 5 : 0;
                    anItem.clientTag = I.InternalId;
                    anItem.bought = I.Bought ? 1 : 0;
                    aList.items.Add(anItem);
                });

                aReq.listsMeta.items.Add(aList);
                aReq.listsMeta.total = aReq.listsMeta.items.Count;
            });

            return aReq;
        }//GenerateRequestDTOForSync

        public void RemoveAll()
        {
            mStorage.ShLists.Clear();
        }

        public void ImportSyncData(string jsonHash, ResSyncDTO dto)
        {
            UpdateStorageHash(jsonHash);

            //delete those that are not returned
            List<string> idsLists = dto.listsMeta.items.Where(z => !string.IsNullOrEmpty(z.clientTag)).Select(x => x.clientTag).ToList();
            mStorage.ShLists.RemoveAll(x => !idsLists.Contains(x.InternalId) && !string.IsNullOrEmpty(x.Id)); //delete those that exists but not the new items that were just created


            dto.listsMeta.items.ForEach(L =>
            {
                ShoppingListDTO wantedlist = mStorage.ShLists.Where(x => x.InternalId == L.clientTag).FirstOrDefault();

                if (wantedlist == null)
                {
                    ShoppingListDTO newList = new ShoppingListDTO()
                    {
                        ListDate = Tools.UnixTimeStampToDateTime(L.created),
                        IsDirty = false,
                        InternalId = Guid.NewGuid().ToString(),
                        Id = L.id,
                        ListDescription = L.description,
                        ListName = L.name
                    };

                    L.items.ForEach(I =>
                    {
                        ItemListDTO lstItem = new ItemListDTO(I);
                        newList.Items.Add(lstItem);
                    });

                    mStorage.ShLists.Add(newList);
                }//new one
                else
                {
                    //update list
                    wantedlist.Id = L.id;
                    wantedlist.ListDescription = L.description;
                    wantedlist.ListName = L.name;

                    //delete listItems those that are not returned
                    List<string> lstItemsRet = L.items.Where(z => !string.IsNullOrEmpty(z.clientTag)).Select(x => x.clientTag).ToList();
                    wantedlist.Items.RemoveAll(x => !lstItemsRet.Contains(x.InternalId) && !string.IsNullOrEmpty(x.ProductId)); //delete those that exists but not the new items that were just created

                    L.items.ForEach(I =>
                    {
                        ItemListDTO wantedItem = wantedlist.Items.FirstOrDefault(x => x.InternalId == I.clientTag);
                        if (wantedItem == null)
                            wantedlist.Items.Add(new ItemListDTO(I));
                        else
                        {
                            wantedItem.Description = I.description;
                            wantedItem.ProductId = I.productId;
                            wantedItem.Quantity = I.quantity;
                            wantedItem.Bought = (I.bought == 1);                            
                        }//else
                    });//items

                    wantedlist.IsDirty = false;//we did the sync
                }//else
            });
        }//ImportSyncData

        public void UpdateStorageHash(string sNewHash)
        {            
            mStorage.SyncJsonHash = sNewHash;
        }//UpdateStorageSelfHash

        public List<ShoppingListDTO> Lists { get { return mStorage.ShLists.OrderBy(x => x.ListDate).ToList(); } }

        public string CurrentJsonHash { get { return mStorage.SyncJsonHash; } }

        public static ListsManager Instance { get { return m_Manager.Value; } }
    }
}
