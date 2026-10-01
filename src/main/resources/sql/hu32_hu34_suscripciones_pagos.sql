IF OBJECT_ID('dbo.pago', 'U') IS NULL
BEGIN

    CREATE TABLE dbo.pago (
        id_pago BIGINT IDENTITY(1,1) NOT NULL PRIMARY KEY,
        monto FLOAT NOT NULL,
        fecha_pago DATETIME2 NOT NULL,
        metodo_pago NVARCHAR(100) NOT NULL,
        estado BIT NOT NULL,
        idUsuario BIGINT NOT NULL,

        CONSTRAINT FK_pago_usuario
            FOREIGN KEY (idUsuario)
            REFERENCES dbo.usuario(idUsuario)
    );

END;
GO

IF OBJECT_ID('dbo.suscripcion', 'U') IS NULL
BEGIN

    CREATE TABLE dbo.suscripcion (
        id_suscripcion INT IDENTITY(1,1) NOT NULL PRIMARY KEY,
        fecha_inicio DATETIME2 NULL,
        fecha_fin DATETIME2 NULL,
        estado BIT NOT NULL DEFAULT 0,
        idAdultoMayor BIGINT NOT NULL,
        id_plan BIGINT NOT NULL,
        id_pago BIGINT NULL,

        CONSTRAINT FK_suscripcion_adulto
            FOREIGN KEY (idAdultoMayor)
            REFERENCES dbo.adulto_mayor(idAdultoMayor),

        CONSTRAINT FK_suscripcion_plan
            FOREIGN KEY (id_plan)
            REFERENCES dbo.[plan](id_plan),

        CONSTRAINT FK_suscripcion_pago
            FOREIGN KEY (id_pago)
            REFERENCES dbo.pago(id_pago)
    );

END;
GO
