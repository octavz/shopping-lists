using CommonBL.Data;
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

        public void DeleteList(ShoppingListDTO aList)
        {
            if (aList == null)
                return;
            m_Lists.Remove(aList);
        }//DeleteList

        public static ListsManager Instance { get { return m_Manager.Value; } }
    }
}
