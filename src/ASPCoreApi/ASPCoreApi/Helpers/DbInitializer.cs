using ASPCoreApi.Models;
using System.Linq;

namespace Model
{
    public class DBIntitializer
    {
        public static void Initialize(DatabaseContext context)
        {
            //Create the db if not yet exists
            context.Database.EnsureCreated();

           
            
        }
    }
}
