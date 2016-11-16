using CommonBL.Data.Request;
using CommonBL.Data.Response;
using CommonBL.Utils;
using Newtonsoft.Json;
using System;
using System.Collections.Generic;
using System.Linq;
using System.Text;


namespace CommonBL.Data
{
    public class ShoppingListDTO
    {

        public ShoppingListDTO()
        {
            ListName = string.Empty;
            IsDirty = false;
        }

        public ShoppingListDTO(ResListDTO aList)
        {
            LoadFromResponse(aList);
        }

        public string Id { get; set; }//DB id

        public string InternalId { get; set; }//Application level only

        public string ListName { get; set; }

        public string ListDescription { get; set; }

        public DateTime ListDate { get; set; }

        public bool IsDirty { get; set; }//used for sync

        public void LoadFromResponse(ResListDTO aList)
        {
            this.Id = aList.Id;
            this.ListDate = Tools.UnixTimeStampToDateTime(aList.CreatedDate);
            this.ListName = aList.Name;
            this.ListDescription = aList.Description;
        }

        public ReqListDTO GenerateRequestFormat(string userId, string token)
        {

            ReqListDTO dto = new ReqListDTO(token)
            {
                CreatedDate = (long)Tools.TimeStampToUnixDateTime(ListDate),
                Id = Id,
                Description = ListDescription,
                Name = string.IsNullOrEmpty(ListName) ? Constants.DEFAULT_LIST_NAME : ListName,
                UserId = userId
            };
            return dto;
        }//GenerateRequestFormat


    }
}