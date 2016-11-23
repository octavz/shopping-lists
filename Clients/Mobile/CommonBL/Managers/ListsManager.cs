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
            }
        }//DeleteList

        public void RemoveAll()
        {
            lock (mLocker)
            {
                mStorage.ShLists.Clear();
            }
        }//RemoveAll

        /// <summary>
        /// SetNoDirty
        /// </summary>
        public void SetNoDirty()
        {
            mStorage.ShLists.ForEach(x => x.IsDirty = false);
        }//SetNoDirty


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

                aReq.listsMeta.items.Add(aList);
                aReq.listsMeta.total = aReq.listsMeta.items.Count;
            });

            return aReq;
        }//GenerateRequestDTOForSync

        public void ImportSyncData(string jsonHash, ResSyncDTO dto)
        {
            RemoveAll();
            mStorage.SyncJsonHash = jsonHash;
            dto.listsMeta.items.ForEach(L =>
            {
                ShoppingListDTO newList = new ShoppingListDTO()
                {
                    ListDate = Tools.UnixTimeStampToDateTime(L.created),
                    IsDirty = false,
                    InternalId = Guid.NewGuid().ToString(),
                    Id = L.id,
                    ListDescription= L.description,
                    ListName = L.name
                };
                mStorage.ShLists.Add(newList);
            });
        }//ImportSyncData

        public List<ShoppingListDTO> Lists { get { return mStorage.ShLists.OrderBy(x => x.ListDate).ToList(); } }

        public string CurrentJsonHash { get { return mStorage.SyncJsonHash; } }

        public static ListsManager Instance { get { return m_Manager.Value; } }
    }
}
