using CommonBL.Data;
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
        private List<ShoppingListDTO> m_Lists = null;

        /// <summary>
        /// ListsManager - constructor
        /// </summary>
        private ListsManager()
        {
            m_Lists = new List<ShoppingListDTO>();            
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
                m_Lists.Add(newList);
                return newList;
            }//lock           
        }//AddNewList

        public void UpdateListName(string UIId, string newName)
        {
            var aList = m_Lists.Where(x => x.InternalId == UIId).FirstOrDefault();
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
            var aList =  m_Lists.Where(x => x.InternalId == UIId).FirstOrDefault();
            if (aList == null)
                return;
            lock (mLocker)
            {
                m_Lists.Remove(aList);
            }
        }//DeleteList

        public void RemoveAll()
        {
            lock (mLocker)
            {
                m_Lists.Clear();
            }
        }//RemoveAll

        public string GetSerializedData()
        {
            var json = JsonConvert.SerializeObject(m_Lists);
            // var content = new StringContent(json, Encoding.UTF8, Constants.MEDIA_TYPE);
            return json;
        }//GetSerializedData

        public void ImportSerializedData(string json)
        {            
            try
            {
                List<ShoppingListDTO> obj = JsonConvert.DeserializeObject<List<ShoppingListDTO>>(json);
                m_Lists = obj;
            }
            catch (Exception e)
            {
                return;
            }
        }//ImportSerializedData

        public List<ShoppingListDTO> Lists { get { return m_Lists.OrderBy(x => x.ListDate).ToList(); } }

        public static ListsManager Instance { get { return m_Manager.Value; } }
    }
}
