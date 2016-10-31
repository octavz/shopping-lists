using CommonBL.Data;
using CommonBL.Data.Response;
using System;
using System.Collections.Generic;
using System.Linq;
using System.Text;
using System.Threading.Tasks;

namespace CommonBL.Managers
{
    public class ListsManager
    {
        private static readonly Lazy<ListsManager> m_Manager = new Lazy<ListsManager>(() => new ListsManager());

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
            ShoppingListDTO newList = new ShoppingListDTO()
            {
                ListDate = DateTime.Now,
            };
            m_Lists.Add(newList);
            return newList;
        }//AddNewList

        public void AddListFromResponse(ResListDTO aRes)
        {
            ShoppingListDTO lstFound = m_Lists.Where(x => x.Id == aRes.Id).FirstOrDefault();
            if (!string.IsNullOrEmpty(aRes.Id) && lstFound != null)
                return;

            m_Lists.Add(new ShoppingListDTO(aRes));
        }//AddListFromResponse

        public void DeleteList(ShoppingListDTO aList)
        {
            if (aList == null)
                return;
            m_Lists.Remove(aList);
        }//DeleteList

        public List<ShoppingListDTO> Lists { get { return m_Lists.OrderBy(x => x.ListDate).ToList(); } }

        public static ListsManager Instance { get { return m_Manager.Value; } }
    }
}
