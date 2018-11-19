using System;
using System.Collections.Generic;
using System.Linq;
using ASP.Helpers;
using ASPCoreApi.Models;

namespace ASP.Services
{
    public interface IStatsService
    {

        Statistics GetById(int id);

    }

    public class StatsService : IStatsService
    {
        private DatabaseContext _context;
        

        public StatsService(DatabaseContext context)
        {
           
            _context = context;
        }

        public Statistics GetById(int id)
        {
            return _context.stats.Find(id);
        }

    }
}