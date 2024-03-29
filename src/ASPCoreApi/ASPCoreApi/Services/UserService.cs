﻿using System;
using System.Collections.Generic;
using System.Linq;
using ASP.Helpers;
using ASPCoreApi.Models;
using ASPCoreApi.Services;

namespace ASP.Services
{
    public interface IUserService
    {
        Users Authenticate(string username, string password);
        IEnumerable<Users> GetAll();
        Users GetById(int id);
        Users Create(Users user, string password);
        Users Update(Users user, string password = null);              
        void Delete(int id);
        List<HighscoresProfile> getTop10(string orderBy);
        HighscoresProfile getUserHsList(string orderBy, int id);
    }

    public class UserService : IUserService
    {
        private DatabaseContext _context;
        private IVisitedSightsService _visitedSightsService;
        private IStatsService _statsService;
        private IGameStatsService _gameStatsService;
        public UserService(DatabaseContext context,IVisitedSightsService visitedSightsService,IStatsService statsService,IGameStatsService gameStats)
        {
            _context = context;
            _visitedSightsService = visitedSightsService;
            _statsService = statsService;
            _gameStatsService = gameStats;
        }

        public Users Authenticate(string username, string password)
        {
            if (string.IsNullOrEmpty(username) || string.IsNullOrEmpty(password))
                return null;

            var user = _context.users.FirstOrDefault(x => x.Username == username);
            
            
            if (user == null)
                return null;

            
            if (!VerifyPasswordHash(password, user.PasswordHash, user.PasswordSalt))
                return null;

            
            
            return user;
        }

        public IEnumerable<Users> GetAll()
        {
            
            var users = _context.users;
          
            return users;
        }

        public Users GetById(int id)
        {
            var userIn = _context.users.Find(id);
                       
            return userIn;
        }

        public Users Create(Users user, string password)
        {
            Boolean hasUpper = false;
            Boolean hasLower = false;
            Boolean hasNumber = false;
            foreach (char var in password)
            {
                if (var.ToString() == char.ToUpper(var).ToString()) hasUpper = true;
                if (var.ToString() == char.ToLower(var).ToString()) hasLower = true;
                if (Char.IsDigit(var)) hasNumber = true;
            }
            
            if (string.IsNullOrWhiteSpace(password))
                throw new AppException("Password is required");

            if (password.Length <= 5 || !hasUpper || !hasLower || !hasNumber)
            {
                throw new AppException("Password must be atleast 6 chars with numbers, lower and uppercase chars");
            }
            if (_context.users.Any(x => x.Username == user.Username))
                throw new AppException("Username: " + user.Username + " is already taken");
            if (_context.users.Any(x => x.Email == user.Email))
                throw new AppException("Email " + user.Email + " is already in use");
            byte[] passwordHash, passwordSalt;
            CreatePasswordHash(password, out passwordHash, out passwordSalt);

            user.PasswordHash = passwordHash;
            user.PasswordSalt = passwordSalt;
            
                            
            _context.users.Add(user);
            _context.SaveChanges();

            return user;
        }

        public Users Update(Users userParam, string password = null)
        {
            var user = _context.users.Find(userParam.Id);           
            if (user == null)
                throw new AppException("User not found");

            if (userParam.Username != user.Username)
            {
                
                if (_context.users.Any(x => x.Username == userParam.Username))
                    throw new AppException("Username " + userParam.Username + " is already taken");
            }

            // update user properties
            if (userParam.FirstName != null && userParam.FirstName != "string" && userParam.FirstName != "") user.FirstName = userParam.FirstName;
            if (userParam.LastName != null && userParam.LastName != "string"&& userParam.LastName != "") user.LastName = userParam.LastName;
            if (userParam.Username != null && userParam.Username != "string" && userParam.Username != "") user.Username = userParam.Username;
            if (userParam.Email != null && userParam.Email != "string" && userParam.Email != "") user.Email = userParam.Email;
            if (userParam.skinId != 0)user.skinId = userParam.skinId;
            
            if (!string.IsNullOrWhiteSpace(password)&& password != "string")
            {
                Boolean hasUpper = false;
                Boolean hasLower = false;
                Boolean hasNumber = false;
                foreach (char var in password)
                {
                    if (var.ToString() == char.ToUpper(var).ToString()) hasUpper = true;
                    if (var.ToString() == char.ToLower(var).ToString()) hasLower = true;
                    if (Char.IsDigit(var)) hasNumber = true;
                }
                
                if (string.IsNullOrWhiteSpace(password))
                    throw new AppException("Password is required");

                if (password.Length <= 5 || !hasUpper || !hasLower || !hasNumber)
                {
                    throw new AppException("Password must be atleast 6 chars with numbers, lower and uppercase chars");
                }
                if (_context.users.Any(x => x.Username == user.Username))
                    throw new AppException("Username: " + user.Username + " is already taken");
                if (_context.users.Any(x => x.Email == user.Email))
                    throw new AppException("Email " + user.Email + " is already in use");
                byte[] passwordHash, passwordSalt;
                CreatePasswordHash(password, out passwordHash, out passwordSalt);

                user.PasswordHash = passwordHash;
                user.PasswordSalt = passwordSalt;
            }
            
            _context.users.Update(user);
            _context.SaveChanges();
            
            return user;
        }
        public void Delete(int id)
        {
            var user = _context.users.Find(id);
            
            if (user != null)
            {
                _visitedSightsService.Delete(user);
                _statsService.Delete(user);
                _gameStatsService.Delete(user);
                _context.users.Remove(user);
                _context.SaveChanges();
            }
        }

        
        private static void CreatePasswordHash(string password, out byte[] passwordHash, out byte[] passwordSalt)
        {
            if (password == null) throw new ArgumentNullException("password");
            if (string.IsNullOrWhiteSpace(password)) throw new ArgumentException("Value cannot be empty or whitespace only string.", "password");

            using (var hmac = new System.Security.Cryptography.HMACSHA512())
            {
                passwordSalt = hmac.Key;
                passwordHash = hmac.ComputeHash(System.Text.Encoding.UTF8.GetBytes(password));
            }
        }

        private static bool VerifyPasswordHash(string password, byte[] storedHash, byte[] storedSalt)
        {
            if (password == null) throw new ArgumentNullException("password");
            if (string.IsNullOrWhiteSpace(password)) throw new ArgumentException("Value cannot be empty or whitespace only string.", "password");
            if (storedHash.Length != 64) throw new ArgumentException("Invalid length of password hash (64 bytes expected).", "passwordHash");
            if (storedSalt.Length != 128) throw new ArgumentException("Invalid length of password salt (128 bytes expected).", "passwordHash");

            using (var hmac = new System.Security.Cryptography.HMACSHA512(storedSalt))
            {
                var computedHash = hmac.ComputeHash(System.Text.Encoding.UTF8.GetBytes(password));
                for (int i = 0; i < computedHash.Length; i++)
                {
                    if (computedHash[i] != storedHash[i]) return false;
                }
            }

            return true;
        }

        public List<HighscoresProfile> getTop10(string orderBy)
        {
            IQueryable<Users> Query = _context.users;
            switch (orderBy)
            {
                case "high":
                    Query = Query.OrderByDescending(d => d.Stats.highestScore);
                    break;
                case "total":
                    Query = Query.OrderByDescending(d => d.Stats.totalScore);
                    break;
            }           
            var userList = Query.ToList();

            foreach (var user in userList)
            {
                user.Stats = _statsService.getByUserId(user.Id);
            }
            var highscoreProfiles = new List<HighscoresProfile>();
            if (userList.Count > 10)
            {
                for (int i = 0; i < 10; i++)
                {
                    var highscore = new HighscoresProfile();
                    highscoreProfiles.Add(highscore);
                    highscoreProfiles[i].ranking = i + 1;
                    highscoreProfiles[i].highestScore = userList[i].Stats.highestScore;
                    highscoreProfiles[i].userName = userList[i].Username;
                    highscoreProfiles[i].totalFailed = userList[i].Stats.totalFailed;
                    highscoreProfiles[i].totalLost = userList[i].Stats.totalLost;
                    highscoreProfiles[i].totalScore = userList[i].Stats.totalScore;
                    highscoreProfiles[i].totalSucces = userList[i].Stats.totalSucces;
                }
            }
            if (userList.Count < 10)
            {
                for (int i = 0; i < userList.Count; i++)
                {
                    var highscore = new HighscoresProfile();
                    highscoreProfiles.Add(highscore);
                    highscoreProfiles[i].ranking = i + 1;
                    highscoreProfiles[i].highestScore = userList[i].Stats.highestScore;
                    highscoreProfiles[i].userName = userList[i].Username;
                    highscoreProfiles[i].totalFailed = userList[i].Stats.totalFailed;
                    highscoreProfiles[i].totalLost = userList[i].Stats.totalLost;
                    highscoreProfiles[i].totalScore = userList[i].Stats.totalScore;
                    highscoreProfiles[i].totalSucces = userList[i].Stats.totalSucces;
                }
            }
            return highscoreProfiles;
        }
        public HighscoresProfile getUserHsList(string orderBy, int id)
        {
            var user = GetById(id);
            var stats = _statsService.getByUserId(id);
            var ranking = 0;
            var highscoreProfile = new HighscoresProfile();
            IQueryable<Users> userQuery = _context.users;
            switch (orderBy)
            {
                case "high":
                    userQuery = userQuery.OrderByDescending(d => d.Stats.highestScore);
                    break;
                case "total":
                    userQuery = userQuery.OrderByDescending(d => d.Stats.totalScore);
                    break;
            }
            var userList = userQuery.ToList();
            foreach (var _user in userList)
            {
                ranking++;
                if(user.Username == _user.Username)
                {
                    highscoreProfile.ranking = ranking;
                    highscoreProfile.highestScore = stats.highestScore;
                    highscoreProfile.totalFailed = stats.totalFailed;
                    highscoreProfile.totalLost = stats.totalLost;
                    highscoreProfile.totalScore = stats.totalScore;
                    highscoreProfile.totalSucces = stats.totalSucces;
                    highscoreProfile.userName = user.Username;
                }
            }
            return highscoreProfile;
        }
    }
}