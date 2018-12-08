using System;
using System.Collections.Generic;
using System.Linq;
using ASP.Helpers;
using ASPCoreApi.Models;

namespace ASP.Services
{
    public interface IStatsService
    {

        Statistics getByUserId(int id);
        Users UpdateStats(Users userParam);
        void Delete(Users userParam)

    }

    public class StatsService : IStatsService
    {
        private DatabaseContext _context;
        

        public StatsService(DatabaseContext context)
        {
           
            _context = context;
        }

        public Statistics getByUserId(int id)
        {
            IQueryable<Statistics> stats = _context.stats;
            var result = stats.Single(d => d.userId == id);
            
            return result;

        }
        public Users UpdateStats(Users userParam)
        {
            var user = _context.users.Find(userParam.Id);
            var stats = getByUserId(userParam.Id);
            if (user == null)
                throw new AppException("User not found");
            if (stats == null)
                throw new AppException("stats not found");

            //update stats
            stats.highestScore = userParam.Stats.highestScore;
            stats.totalScore = userParam.Stats.totalScore;
            stats.totalSucces = userParam.Stats.totalSucces;
            stats.totalFailed = userParam.Stats.totalFailed;
            stats.totalLost = userParam.Stats.totalLost;


            _context.stats.Update(stats);            
            _context.SaveChanges();
            user.Stats = stats;
            return user;
        }
        public void Delete(Users userParam)
        {
            var stats = getByUserId(userParam.Id);

            if (stats != null)
            {
                _context.stats.Remove(stats);            
                _context.SaveChanges();
            }
        }

    }
}