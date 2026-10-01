IF OBJECT_ID('dbo.recuerdo', 'U') IS NULL
BEGIN
    CREATE TABLE dbo.recuerdo (
        id_recuerdo BIGINT IDENTITY(1,1) PRIMARY KEY,
        titulo_recuerdo NVARCHAR(255) NOT NULL,
        tipo_recuerdo NVARCHAR(50) NOT NULL,
        contenido NVARCHAR(MAX) NOT NULL,
        formato NVARCHAR(100) NULL,
        favorito BIT NOT NULL DEFAULT 0,
        fechaCreacion DATETIME2 NOT NULL,
        idAdultoMayor BIGINT NOT NULL,
        idAsignacion BIGINT NULL,
        CONSTRAINT FK_recuerdo_adulto_mayor
            FOREIGN KEY (idAdultoMayor)
            REFERENCES dbo.adulto_mayor(idAdultoMayor),
        CONSTRAINT FK_recuerdo_asignacion
            FOREIGN KEY (idAsignacion)
            REFERENCES dbo.asignacion(idAsignacion)
    );
END;
