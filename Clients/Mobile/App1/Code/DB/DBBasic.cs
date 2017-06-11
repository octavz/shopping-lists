using ShList.Code.DB.Data;
using SQLite;
using System;
using System.Collections.Generic;
using System.Linq;
using System.Text;

//https://developer.xamarin.com/guides/android/application_fundamentals/data/part_3_using_sqlite_orm/

namespace ShList.Code.DB
{
    public abstract class DBBasic
    {
        public const string DB_NAME = "SH.data";

        /// <summary>
        /// GetDBPath
        /// </summary>
        /// <returns></returns>
        protected string GetDBPath()
        {
            string dbPath = System.IO.Path.Combine(Environment.GetFolderPath(Environment.SpecialFolder.Personal), DB_NAME);
            return dbPath;
        }//GetDBPath

        /// <summary>
        /// CreateDB
        /// </summary>
        protected void CreateDB(List<DBProduct> lstDBProds)
        {
            var db = new SQLiteConnection(GetDBPath());
            db.CreateTable<DBProduct>();
            if (lstDBProds != null && lstDBProds.Count > 0)
                lstDBProds.ForEach(x => db.Insert(x));
        }//CreateDB
    }
}