IF OBJECT_ID(N'[__EFMigrationsHistory]') IS NULL
BEGIN
    CREATE TABLE [__EFMigrationsHistory] (
        [MigrationId] nvarchar(150) NOT NULL,
        [ProductVersion] nvarchar(32) NOT NULL,
        CONSTRAINT [PK___EFMigrationsHistory] PRIMARY KEY ([MigrationId])
    );
END;

GO

IF NOT EXISTS(SELECT * FROM [__EFMigrationsHistory] WHERE [MigrationId] = N'20181119135005_stats')
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

IF NOT EXISTS(SELECT * FROM [__EFMigrationsHistory] WHERE [MigrationId] = N'20181119135005_stats')
BEGIN
    CREATE TABLE [stats] (
        [StatisticsId] int NOT NULL IDENTITY,
        [highestScore] int NOT NULL,
        [totalScore] int NOT NULL,
        [totalFailed] int NOT NULL,
        [totalSucces] int NOT NULL,
        [totalLost] int NOT NULL,
        CONSTRAINT [PK_stats] PRIMARY KEY ([StatisticsId])
    );
END;

GO

IF NOT EXISTS(SELECT * FROM [__EFMigrationsHistory] WHERE [MigrationId] = N'20181119135005_stats')
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
        [StatsStatisticsId] int NULL,
        CONSTRAINT [PK_users] PRIMARY KEY ([Id]),
        CONSTRAINT [FK_users_stats_StatsStatisticsId] FOREIGN KEY ([StatsStatisticsId]) REFERENCES [stats] ([StatisticsId]) ON DELETE NO ACTION
    );
END;

GO

IF NOT EXISTS(SELECT * FROM [__EFMigrationsHistory] WHERE [MigrationId] = N'20181119135005_stats')
BEGIN
    CREATE INDEX [IX_users_StatsStatisticsId] ON [users] ([StatsStatisticsId]);
END;

GO

IF NOT EXISTS(SELECT * FROM [__EFMigrationsHistory] WHERE [MigrationId] = N'20181119135005_stats')
BEGIN
    INSERT INTO [__EFMigrationsHistory] ([MigrationId], [ProductVersion])
    VALUES (N'20181119135005_stats', N'2.1.4-rtm-31024');
END;

GO

IF NOT EXISTS(SELECT * FROM [__EFMigrationsHistory] WHERE [MigrationId] = N'20181119135429_stats2')
BEGIN
    ALTER TABLE [users] DROP CONSTRAINT [FK_users_stats_StatsStatisticsId];
END;

GO

IF NOT EXISTS(SELECT * FROM [__EFMigrationsHistory] WHERE [MigrationId] = N'20181119135429_stats2')
BEGIN
    EXEC sp_rename N'[users].[StatsStatisticsId]', N'StatsId', N'COLUMN';
END;

GO

IF NOT EXISTS(SELECT * FROM [__EFMigrationsHistory] WHERE [MigrationId] = N'20181119135429_stats2')
BEGIN
    EXEC sp_rename N'[users].[IX_users_StatsStatisticsId]', N'IX_users_StatsId', N'INDEX';
END;

GO

IF NOT EXISTS(SELECT * FROM [__EFMigrationsHistory] WHERE [MigrationId] = N'20181119135429_stats2')
BEGIN
    EXEC sp_rename N'[stats].[StatisticsId]', N'Id', N'COLUMN';
END;

GO

IF NOT EXISTS(SELECT * FROM [__EFMigrationsHistory] WHERE [MigrationId] = N'20181119135429_stats2')
BEGIN
    ALTER TABLE [users] ADD CONSTRAINT [FK_users_stats_StatsId] FOREIGN KEY ([StatsId]) REFERENCES [stats] ([Id]) ON DELETE NO ACTION;
END;

GO

IF NOT EXISTS(SELECT * FROM [__EFMigrationsHistory] WHERE [MigrationId] = N'20181119135429_stats2')
BEGIN
    INSERT INTO [__EFMigrationsHistory] ([MigrationId], [ProductVersion])
    VALUES (N'20181119135429_stats2', N'2.1.4-rtm-31024');
END;

GO

IF NOT EXISTS(SELECT * FROM [__EFMigrationsHistory] WHERE [MigrationId] = N'20181119141317_stats3')
BEGIN
    INSERT INTO [__EFMigrationsHistory] ([MigrationId], [ProductVersion])
    VALUES (N'20181119141317_stats3', N'2.1.4-rtm-31024');
END;

GO

