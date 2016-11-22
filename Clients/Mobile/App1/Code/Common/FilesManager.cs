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
using System.IO;
using ShList.Extended.Code;

namespace ShList.Code.Common
{
    public class FilesManager
    {
        private const string FILE_SH_LISTS_STATE = "lists.json";        

        private static Object m_locker = new object();

        public static void WriteShListsState(string serializedObj)
        {
            var path = ShAppContext.GetExternalFilesDir(null) + "/"+ShAppContext.UserId;
            var filename = Path.Combine(path, FILE_SH_LISTS_STATE);
            lock (m_locker)
            {
                if (!Android.OS.Environment.ExternalStorageDirectory.CanWrite())
                    return;
                if (!Directory.Exists(path))
                    Directory.CreateDirectory(path);
                if (File.Exists(filename))
                    File.Delete(filename);
                using (var streamWriter = new StreamWriter(filename, true))
                {
                    streamWriter.Write(serializedObj);
                }
            }//lock
        }//WriteShListsState

        public static string ReadShListsState()
        {
            var path = ShAppContext.GetExternalFilesDir(null) + "/" + ShAppContext.UserId;
            var filename = Path.Combine(path, FILE_SH_LISTS_STATE);
            if (!File.Exists(filename))
                return null;
            lock (m_locker)
            {
                using (var streamReader = new StreamReader(filename))
                {
                    return streamReader.ReadToEnd();
                }
            }
        }//ReadShListsState

        private static  ShApplication ShAppContext { get { return (ShApplication)Application.Context ; } }
    }
}