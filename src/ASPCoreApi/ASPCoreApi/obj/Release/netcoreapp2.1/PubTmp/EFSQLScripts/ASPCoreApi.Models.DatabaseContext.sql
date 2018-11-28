IF OBJECT_ID(N'[__EFMigrationsHistory]') IS NULL
BEGIN
    CREATE TABLE [__EFMigrationsHistory] (
        [MigrationId] nvarchar(150) NOT NULL,
        [ProductVersion] nvarchar(32) NOT NULL,
        CONSTRAINT [PK___EFMigrationsHistory] PRIMARY KEY ([MigrationId])
    );
END;

GO

IF NOT EXISTS(SELECT * FROM [__EFMigrationsHistory] WHERE [MigrationId] = N'20181127200242_skinId')
BEGIN
    CREATE TABLE [dots] (
        [id] int NOT NULL IDENTITY,
        [Latitude] nvarchar(max) NULL,
        [Longitude] nvarchar(max) NULL,
        [Taken] bit NOT NULL,
        CONSTRAINT [PK_dots] PRIMARY KEY ([id])
    );
END;

GO

IF NOT EXISTS(SELECT * FROM [__EFMigrationsHistory] WHERE [MigrationId] = N'20181127200242_skinId')
BEGIN
    CREATE TABLE [gameStats] (
        [Id] int NOT NULL IDENTITY,
        [lifePoints] int NOT NULL,
        [rifle] int NOT NULL,
        [pushBackGun] int NOT NULL,
        [freezeGun] int NOT NULL,
        CONSTRAINT [PK_gameStats] PRIMARY KEY ([Id])
    );
END;

GO

IF NOT EXISTS(SELECT * FROM [__EFMigrationsHistory] WHERE [MigrationId] = N'20181127200242_skinId')
BEGIN
    CREATE TABLE [sights] (
        [id] int NOT NULL IDENTITY,
        [Name] nvarchar(max) NULL,
        [shortDescription] nvarchar(max) NULL,
        [longDescription] nvarchar(max) NULL,
        [Longitude] nvarchar(max) NULL,
        [Latitude] nvarchar(max) NULL,
        [sightImage] nvarchar(max) NULL,
        [Website] nvarchar(max) NULL,
        CONSTRAINT [PK_sights] PRIMARY KEY ([id])
    );
END;

GO

IF NOT EXISTS(SELECT * FROM [__EFMigrationsHistory] WHERE [MigrationId] = N'20181127200242_skinId')
BEGIN
    CREATE TABLE [stats] (
        [Id] int NOT NULL IDENTITY,
        [highestScore] int NOT NULL,
        [totalScore] int NOT NULL,
        [totalFailed] int NOT NULL,
        [totalSucces] int NOT NULL,
        [totalLost] int NOT NULL,
        CONSTRAINT [PK_stats] PRIMARY KEY ([Id])
    );
END;

GO

IF NOT EXISTS(SELECT * FROM [__EFMigrationsHistory] WHERE [MigrationId] = N'20181127200242_skinId')
BEGIN
    CREATE TABLE [users] (
        [Id] int NOT NULL IDENTITY,
        [FirstName] nvarchar(max) NULL,
        [LastName] nvarchar(max) NULL,
        [Username] nvarchar(max) NULL,
        [Email] nvarchar(max) NULL,
        [PasswordHash] varbinary(max) NULL,
        [PasswordSalt] varbinary(max) NULL,
        [accessLevel] int NOT NULL,
        [skinId] int NOT NULL,
        [StatsId] int NOT NULL,
        [gameStatsId] int NOT NULL,
        CONSTRAINT [PK_users] PRIMARY KEY ([Id]),
        CONSTRAINT [FK_users_stats_StatsId] FOREIGN KEY ([StatsId]) REFERENCES [stats] ([Id]) ON DELETE CASCADE,
        CONSTRAINT [FK_users_gameStats_gameStatsId] FOREIGN KEY ([gameStatsId]) REFERENCES [gameStats] ([Id]) ON DELETE CASCADE
    );
END;

GO

IF NOT EXISTS(SELECT * FROM [__EFMigrationsHistory] WHERE [MigrationId] = N'20181127200242_skinId')
BEGIN
    CREATE INDEX [IX_users_StatsId] ON [users] ([StatsId]);
END;

GO

IF NOT EXISTS(SELECT * FROM [__EFMigrationsHistory] WHERE [MigrationId] = N'20181127200242_skinId')
BEGIN
    CREATE INDEX [IX_users_gameStatsId] ON [users] ([gameStatsId]);
END;

GO

IF NOT EXISTS(SELECT * FROM [__EFMigrationsHistory] WHERE [MigrationId] = N'20181127200242_skinId')
BEGIN
    INSERT INTO [__EFMigrationsHistory] ([MigrationId], [ProductVersion])
    VALUES (N'20181127200242_skinId', N'2.1.4-rtm-31024');
END;

GO

