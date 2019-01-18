using ASP.Helpers;
using ASPCoreApi.Models;
using System;
using System.Collections.Generic;
using System.Linq;
using System.Threading.Tasks;

namespace ASPCoreApi.Services
{
    public interface IGameStatsService
    {

        GameStats getByUserId(int id);
        Users UpdateGameStats(Users userParam);
        void Delete(Users userParam);

    }
    public class GameStatsService : IGameStatsService
    {
        private DatabaseContext _context;

        public GameStatsService(DatabaseContext context)
        {
            _context = context;
        }

        public GameStats getByUserId(int id)
        {
            IQueryable<GameStats> gamestats = _context.gameStats;
            var result = gamestats.Single(d => d.userId == id);

            return result;
        }

        public Users UpdateGameStats(Users userParam)
        {
            var user = _context.users.Find(userParam.Id);
            var gameStats = getByUserId(userParam.Id);
            if (user == null)
                throw new AppException("User not found");
            if (gameStats == null)
                throw new AppException("gamestats not found");

            //update stats
            gameStats.lifePoints = userParam.gameStats.lifePoints;
            gameStats.rifle = userParam.gameStats.rifle;
            gameStats.freezeGun = userParam.gameStats.freezeGun;
            gameStats.pushBackGun = userParam.gameStats.pushBackGun;
            gameStats.coins = userParam.gameStats.coins;

            _context.gameStats.Update(gameStats);
            _context.users.Update(user);
            _context.SaveChanges();
            user.gameStats = gameStats;
            return user;
        }
        public void Delete(Users userParam)
        {
            var gameStats = getByUserId(userParam.Id);

            if (gameStats != null)
            {
                _context.gameStats.Remove(gameStats);
                _context.SaveChanges();
            }
        }
    }
}
