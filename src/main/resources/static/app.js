(() => {
  const API = window.SOULSTORY_API_URL || (window.location.port === "4200" ? "http://localhost:8080/api" : "/api");
  const SESSION_KEY = "soulstory.session";
  const app = document.querySelector("#app");
  const topbarUser = document.querySelector("#topbar-user");
  const toast = document.querySelector("#toast");
  let session = readSession();
  let memories = [];
  let activeAdult = null;
  let caregiverId = null;
  let assignedAdults = [];
  let toastTimer;

  function readSession() {
    try {
      return JSON.parse(sessionStorage.getItem(SESSION_KEY) || "null");
    } catch {
      sessionStorage.removeItem(SESSION_KEY);
      return null;
    }
  }

  function safeText(value) {
    return String(value ?? "");
  }

  function showToast(message, isError = false) {
    toast.textContent = message;
    toast.classList.toggle("is-error", isError);
    toast.classList.add("is-visible");
    window.clearTimeout(toastTimer);
    toastTimer = window.setTimeout(() => toast.classList.remove("is-visible"), 3600);
  }

  async function request(path, options = {}) {
    const headers = new Headers(options.headers || {});
    if (session?.token) headers.set("Authorization", `Bearer ${session.token}`);
    if (options.body && !(options.body instanceof FormData)) {
      headers.set("Content-Type", "application/json");
    }
    const response = await fetch(`${API}${path}`, { ...options, headers });
    if (response.status === 401) {
      clearSession();
      throw new Error("La sesión caducó. Inicia sesión otra vez.");
    }
    const body = await response.text();
    let data = null;
    if (body) {
      try {
        data = JSON.parse(body);
      } catch {
        data = body;
      }
    }
    if (!response.ok) {
      const message = typeof data === "string"
        ? data
        : data?.mensaje || data?.message || data?.error || `No se pudo completar la solicitud (${response.status}).`;
      throw new Error(message);
    }
    return data;
  }

  function clearSession() {
    sessionStorage.removeItem(SESSION_KEY);
    session = null;
    memories = [];
    activeAdult = null;
    caregiverId = null;
    assignedAdults = [];
    render();
  }

  function initials(name) {
    return safeText(name).trim().split(/\s+/).slice(0, 2).map((part) => part[0] || "").join("").toUpperCase() || "S";
  }

  function setTopbar() {
    topbarUser.replaceChildren();
    topbarUser.hidden = !session;
    if (!session) return;
    const avatar = document.createElement("span");
    avatar.className = "avatar";
    avatar.textContent = initials(session.nombreCompleto);
    const greeting = document.createElement("span");
    greeting.textContent = session.nombreCompleto;
    topbarUser.append(avatar, greeting);
  }

  function renderAuth(mode = "login") {
    const registering = mode === "register";
    app.innerHTML = `
      <section class="auth-layout">
        <div class="auth-story">
          <p class="eyebrow">Recuerdos para compartir</p>
          <h1>Las historias de una vida merecen <em>quedarse.</em></h1>
          <p class="auth-copy">Guarda esos momentos que hacen única a cada familia. Un recuerdo, una voz, una historia: todo tiene un lugar aquí.</p>
          <div class="quote-card">
            <p>“Las cosas que recordamos son las que nos hacen quienes somos.”</p>
            <span>Un espacio para cada historia</span>
          </div>
        </div>
        <section class="auth-card" aria-labelledby="auth-title">
          <h2 id="auth-title">${registering ? "Crea tu cuenta" : "Qué bueno tenerte de vuelta"}</h2>
          <p class="auth-subtitle">${registering ? "Empieza a guardar historias que merecen ser recordadas." : "Ingresa a tu espacio de recuerdos."}</p>
          <form id="auth-form">
            ${registering ? `
              <div class="field">
                <label for="name">Nombre completo</label>
                <input id="name" name="nombreCompleto" autocomplete="name" placeholder="Tu nombre" required>
              </div>` : ""}
            <div class="field">
              <label for="email">Correo electrónico</label>
              <input id="email" name="email" type="email" autocomplete="email" placeholder="nombre@correo.com" required>
            </div>
            <div class="field">
              <label for="password">Contraseña</label>
              <input id="password" name="contrasena" type="password" autocomplete="${registering ? "new-password" : "current-password"}" minlength="${registering ? "8" : "1"}" placeholder="${registering ? "Al menos 8 caracteres" : "Tu contraseña"}" required>
            </div>
            ${registering ? `
              <div class="field">
                <label for="role">¿Cómo usarás SoulStory?</label>
                <select id="role" name="rol" required>
                  <option value="ADULTO_MAYOR">Adulto mayor</option>
                  <option value="CUIDADOR">Cuidador</option>
                  <option value="FAMILIAR">Familiar</option>
                </select>
              </div>` : ""}
            <p class="form-error" id="auth-error" role="alert"></p>
            <button class="button button-primary button-full" type="submit">${registering ? "Crear mi cuenta" : "Ingresar"} <span aria-hidden="true">→</span></button>
          </form>
          <p class="auth-switch">
            ${registering ? "¿Ya tienes una cuenta?" : "¿Es tu primera vez por aquí?"}
            <button class="text-button" id="auth-switch" type="button">${registering ? "Inicia sesión" : "Crear cuenta"}</button>
          </p>
        </section>
      </section>`;

    document.querySelector("#auth-switch").addEventListener("click", () => renderAuth(registering ? "login" : "register"));
    document.querySelector("#auth-form").addEventListener("submit", (event) => submitAuth(event, registering));
  }

  async function submitAuth(event, registering) {
    event.preventDefault();
    const form = event.currentTarget;
    const submit = form.querySelector('button[type="submit"]');
    const error = document.querySelector("#auth-error");
    error.textContent = "";
    submit.disabled = true;
    try {
      const values = Object.fromEntries(new FormData(form).entries());
      const data = await request(registering ? "/usuarios/registro" : "/usuarios/login", {
        method: "POST",
        body: JSON.stringify(values)
      });
      if (registering) {
        showToast("Cuenta creada. Ya puedes iniciar sesión.");
        renderAuth("login");
        const email = document.querySelector("#email");
        if (email) email.value = values.email;
        return;
      }
      if (!data?.token) throw new Error("El servidor no devolvió un token de sesión.");
      session = data;
      sessionStorage.setItem(SESSION_KEY, JSON.stringify(session));
      showToast(`¡Hola, ${session.nombreCompleto}!`);
      await render();
    } catch (err) {
      error.textContent = err.message || "No se pudo completar el acceso.";
    } finally {
      if (submit.isConnected) submit.disabled = false;
    }
  }

  function isCaregiver() {
    return safeText(session?.rol).toLocaleUpperCase("es").includes("CUIDADOR");
  }

  async function render() {
    setTopbar();
    if (!session?.token) {
      renderAuth();
      return;
    }
    if (isCaregiver() && caregiverId && assignedAdults.length === 0) {
      caregiverId = null;
    }
    if (isCaregiver() && !caregiverId) {
      renderCaregiverSetup();
      return;
    }
    if (!activeAdult && !isCaregiver()) {
      renderAdultSetup();
      return;
    }
    renderDashboard();
    if (activeAdult) await loadMemories();
  }

  function renderAdultSetup(errorMessage = "") {
    app.innerHTML = `
      <section class="auth-layout">
        <div class="auth-story">
          <p class="eyebrow">Tu espacio personal</p>
          <h1>Un hogar para tus <em>recuerdos.</em></h1>
          <p class="auth-copy">Conecta tu perfil de adulto mayor para ver las historias que ya guardaste y crear nuevas.</p>
        </div>
        <section class="auth-card">
          <h2>Conecta tu perfil</h2>
          <p class="auth-subtitle">Ingresa el ID de adulto mayor que te asignó tu administrador.</p>
          <form id="adult-form">
            <div class="field">
              <label for="adult-id">ID de adulto mayor</label>
              <input id="adult-id" name="adultId" inputmode="numeric" pattern="[0-9]+" placeholder="Ejemplo: 1" required>
            </div>
            <p class="form-error" id="setup-error" role="alert">${escapeHtml(errorMessage)}</p>
            <button class="button button-primary button-full" type="submit">Continuar <span aria-hidden="true">→</span></button>
          </form>
          <p class="auth-switch"><button class="text-button" id="logout-link" type="button">Cerrar sesión</button></p>
        </section>
      </section>`;
    document.querySelector("#adult-form").addEventListener("submit", (event) => {
      event.preventDefault();
      const value = new FormData(event.currentTarget).get("adultId").toString().trim();
      if (!/^\d+$/.test(value)) {
        document.querySelector("#setup-error").textContent = "Ingresa un ID numérico válido.";
        return;
      }
      activeAdult = { idAdultoMayor: value, nombreCompleto: session.nombreCompleto };
      sessionStorage.setItem("soulstory.adult", JSON.stringify(activeAdult));
      render();
    });
    document.querySelector("#logout-link").addEventListener("click", clearSession);
  }

  async function renderCaregiverSetup(errorMessage = "") {
    app.innerHTML = `
      <section class="auth-layout">
        <div class="auth-story">
          <p class="eyebrow">Acompañar también es recordar</p>
          <h1>Historias que se cuidan <em>juntos.</em></h1>
          <p class="auth-copy">Elige a la persona a quien acompañas y guarda con ella esos momentos que no quieren olvidar.</p>
        </div>
        <section class="auth-card">
          <h2>Elige un perfil</h2>
          <p class="auth-subtitle">Ingresa tu ID de cuidador para cargar las personas que tienes asignadas.</p>
          <form id="caregiver-form">
            <div class="field">
              <label for="caregiver-id">ID de cuidador</label>
              <input id="caregiver-id" name="caregiverId" inputmode="numeric" pattern="[0-9]+" placeholder="Ejemplo: 1" required>
            </div>
            <p class="form-error" id="setup-error" role="alert">${escapeHtml(errorMessage)}</p>
            <button class="button button-primary button-full" type="submit">Buscar perfiles <span aria-hidden="true">→</span></button>
          </form>
          <p class="auth-switch"><button class="text-button" id="logout-link" type="button">Cerrar sesión</button></p>
        </section>
      </section>`;
    document.querySelector("#caregiver-form").addEventListener("submit", loadAssignedAdults);
    document.querySelector("#logout-link").addEventListener("click", clearSession);
  }

  async function loadAssignedAdults(event) {
    event.preventDefault();
    const form = event.currentTarget;
    const submit = form.querySelector('button[type="submit"]');
    const error = document.querySelector("#setup-error");
    const id = new FormData(form).get("caregiverId").toString().trim();
    error.textContent = "";
    submit.disabled = true;
    try {
      const result = await request(`/cuidadores/${encodeURIComponent(id)}/adultos-mayores`);
      if (!Array.isArray(result) || result.length === 0) {
        error.textContent = "No se encontraron perfiles asignados a ese cuidador.";
        return;
      }
      caregiverId = id;
      assignedAdults = result;
      activeAdult = assignedAdults[0];
      render();
    } catch (err) {
      error.textContent = err.message || "No se pudieron cargar los perfiles.";
    } finally {
      if (submit.isConnected) submit.disabled = false;
    }
  }

  function renderDashboard() {
    const selectedName = activeAdult?.nombreCompleto || "Mis recuerdos";
    const profileOptions = isCaregiver()
      ? `<div class="field"><select id="adult-select" aria-label="Selecciona un adulto mayor">${assignedAdults.map((adult) =>
          `<option value="${escapeHtml(adult.idAdultoMayor)}" ${String(adult.idAdultoMayor) === String(activeAdult?.idAdultoMayor) ? "selected" : ""}>${escapeHtml(adult.nombreCompleto)}</option>`
        ).join("")}</select></div>`
      : `<div class="profile-label">Perfil conectado: <strong>${escapeHtml(selectedName)}</strong> · ID ${escapeHtml(activeAdult.idAdultoMayor)}</div>`;
    app.innerHTML = `
      <section class="dashboard-header">
        <div>
          <p class="eyebrow">Tu biblioteca de vida</p>
          <h1>Hola, ${escapeHtml(firstName(session.nombreCompleto))}<span class="brand-period">.</span></h1>
          <p>Cada recuerdo guarda un pedacito de lo que somos.</p>
        </div>
        <div class="dashboard-actions">
          <button class="button button-soft" id="change-profile" type="button">Cambiar perfil</button>
          <button class="button button-primary" id="new-memory" type="button"><span aria-hidden="true">＋</span> Nuevo recuerdo</button>
        </div>
      </section>
      <section class="profile-bar">
        <span class="profile-icon" aria-hidden="true">♡</span>
        ${profileOptions}
      </section>
      <section class="stat-grid" aria-label="Resumen de recuerdos">
        <article class="stat-card"><span class="stat-icon" aria-hidden="true">▧</span><div><p class="stat-number" id="stat-total">—</p><p class="stat-label">Recuerdos guardados</p></div></article>
        <article class="stat-card"><span class="stat-icon" aria-hidden="true">♡</span><div><p class="stat-number" id="stat-favorites">—</p><p class="stat-label">Favoritos</p></div></article>
        <article class="stat-card"><span class="stat-icon" aria-hidden="true">✎</span><div><p class="stat-number" id="stat-text">—</p><p class="stat-label">Historias escritas</p></div></article>
      </section>
      <section class="content-panel">
        <div class="panel-heading">
          <div><h2>La caja de recuerdos</h2><p>Momentos, voces e historias para volver a vivir.</p></div>
          <div class="panel-tools">
            <input id="memory-search" class="search-input" type="search" placeholder="Buscar un recuerdo…" aria-label="Buscar recuerdos">
            <select id="memory-filter" class="filter-select" aria-label="Filtrar recuerdos">
              <option value="TODOS">Todos</option>
              <option value="FAVORITOS">Favoritos</option>
              <option value="TEXTO">Historias</option>
              <option value="AUDIO">Audios</option>
              <option value="IMAGEN">Fotos</option>
            </select>
          </div>
        </div>
        <div id="memory-list" class="loading-state" aria-live="polite"><span class="loading-dot" aria-label="Cargando"></span></div>
      </section>`;

    document.querySelector("#new-memory").addEventListener("click", openMemoryForm);
    document.querySelector("#change-profile").addEventListener("click", changeProfile);
    document.querySelector("#memory-search").addEventListener("input", renderMemories);
    document.querySelector("#memory-filter").addEventListener("change", renderMemories);
    document.querySelector("#adult-select")?.addEventListener("change", (event) => {
      activeAdult = assignedAdults.find((adult) => String(adult.idAdultoMayor) === event.target.value) || null;
      renderDashboard();
      loadMemories();
    });
  }

  function firstName(name) {
    return safeText(name).trim().split(/\s+/)[0] || "bienvenido";
  }

  function escapeHtml(value) {
    return safeText(value).replace(/[&<>"']/g, (char) => ({
      "&": "&amp;", "<": "&lt;", ">": "&gt;", '"': "&quot;", "'": "&#39;"
    })[char]);
  }

  async function loadMemories() {
    if (!activeAdult) return;
    const list = document.querySelector("#memory-list");
    if (list) list.innerHTML = '<div class="loading-state"><span class="loading-dot" aria-label="Cargando"></span></div>';
    try {
      const result = await request(`/adultos-mayores/${encodeURIComponent(activeAdult.idAdultoMayor)}/recuerdos`);
      memories = Array.isArray(result) ? result : [];
      updateStats();
      renderMemories();
    } catch (error) {
      if (!document.querySelector("#memory-list")) return;
      document.querySelector("#memory-list").innerHTML = `
        <div class="empty-state"><div><div class="empty-icon">!</div><h3>No pudimos cargar los recuerdos</h3>
        <p>${escapeHtml(error.message)} Comprueba que el perfil esté creado y tengas acceso.</p></div></div>`;
    }
  }

  function updateStats() {
    const total = document.querySelector("#stat-total");
    if (!total) return;
    total.textContent = String(memories.length);
    document.querySelector("#stat-favorites").textContent = String(memories.filter((memory) => memory.favorito).length);
    document.querySelector("#stat-text").textContent = String(memories.filter((memory) => memory.tipoRecuerdo === "TEXTO").length);
  }

  function renderMemories() {
    const list = document.querySelector("#memory-list");
    if (!list) return;
    const query = document.querySelector("#memory-search").value.trim().toLocaleLowerCase("es");
    const filter = document.querySelector("#memory-filter").value;
    const visible = memories.filter((memory) => {
      const matchesText = `${memory.tituloRecuerdo || ""} ${memory.contenido || ""}`.toLocaleLowerCase("es").includes(query);
      const matchesType = filter === "TODOS" || (filter === "FAVORITOS" ? memory.favorito : memory.tipoRecuerdo === filter);
      return matchesText && matchesType;
    });
    if (!visible.length) {
      list.className = "empty-state";
      list.innerHTML = memories.length
        ? '<div><div class="empty-icon">⌕</div><h3>No encontramos ese recuerdo</h3><p>Prueba otra búsqueda o cambia el filtro.</p></div>'
        : '<div><div class="empty-icon">♡</div><h3>Aquí comienza tu historia</h3><p>Todavía no hay recuerdos guardados en este perfil. Crea el primero y vuelve a visitarlo cuando quieras.</p><button class="button button-primary" id="empty-create" type="button">Crear primer recuerdo <span aria-hidden="true">→</span></button></div>';
      document.querySelector("#empty-create")?.addEventListener("click", openMemoryForm);
      return;
    }
    list.className = "memory-grid";
    list.replaceChildren(...visible.map(createMemoryCard));
  }

  function createMemoryCard(memory) {
    const card = document.createElement("article");
    card.className = "memory-card";
    const meta = document.createElement("div");
    meta.className = "memory-meta";
    const type = document.createElement("span");
    type.className = "memory-type";
    type.textContent = memory.tipoRecuerdo || "RECUERDO";
    const favorite = document.createElement("button");
    favorite.className = `button button-icon${memory.favorito ? " is-favorite" : ""}`;
    favorite.type = "button";
    favorite.setAttribute("aria-label", memory.favorito ? "Quitar de favoritos" : "Marcar como favorito");
    favorite.setAttribute("aria-pressed", String(Boolean(memory.favorito)));
    favorite.textContent = memory.favorito ? "♥" : "♡";
    favorite.addEventListener("click", () => toggleFavorite(memory, favorite));
    meta.append(type, favorite);

    const title = document.createElement("h3");
    title.textContent = memory.tituloRecuerdo || "Recuerdo sin título";
    const content = document.createElement("p");
    content.className = "memory-content";
    content.textContent = memory.tipoRecuerdo === "TEXTO" ? memory.contenido || "" : "Un momento guardado para volver a vivirlo.";
    const footer = document.createElement("div");
    footer.className = "memory-footer";
    const date = document.createElement("span");
    date.className = "memory-date";
    date.textContent = `◷  ${formatDate(memory.fechaCreacion)}`;
    footer.append(date);
    if (["AUDIO", "IMAGEN"].includes(memory.tipoRecuerdo)) {
      const file = document.createElement("a");
      file.className = "text-button";
      file.href = `${API}/recuerdos/${encodeURIComponent(memory.idRecuerdo)}/archivo`;
      file.target = "_blank";
      file.rel = "noopener";
      file.textContent = memory.tipoRecuerdo === "AUDIO" ? "Escuchar ↗" : "Ver imagen ↗";
      file.addEventListener("click", (event) => {
        event.preventDefault();
        openProtectedFile(memory);
      });
      footer.append(file);
    }
    card.append(meta, title, content, footer);
    return card;
  }

  function formatDate(value) {
    if (!value) return "Fecha no disponible";
    const date = new Date(value);
    return Number.isNaN(date.getTime()) ? "Fecha no disponible" : new Intl.DateTimeFormat("es", { day: "numeric", month: "short", year: "numeric" }).format(date);
  }

  async function toggleFavorite(memory, button) {
    button.disabled = true;
    try {
      const path = isCaregiver()
        ? `/cuidadores/${encodeURIComponent(caregiverId)}/recuerdos/${encodeURIComponent(memory.idRecuerdo)}/favorito`
        : `/adultos-mayores/${encodeURIComponent(activeAdult.idAdultoMayor)}/recuerdos/${encodeURIComponent(memory.idRecuerdo)}/favorito`;
      const updated = await request(path, { method: "PATCH" });
      memories = memories.map((item) => item.idRecuerdo === memory.idRecuerdo ? updated : item);
      updateStats();
      renderMemories();
    } catch (error) {
      showToast(error.message || "No se pudo actualizar el favorito.", true);
      button.disabled = false;
    }
  }

  async function openProtectedFile(memory) {
    try {
      const response = await fetch(`${API}/recuerdos/${encodeURIComponent(memory.idRecuerdo)}/archivo`, {
        headers: { Authorization: `Bearer ${session.token}` }
      });
      if (!response.ok) {
        if (response.status === 401) clearSession();
        throw new Error("No se pudo abrir el archivo del recuerdo.");
      }
      const blob = await response.blob();
      const fileUrl = URL.createObjectURL(blob);
      window.open(fileUrl, "_blank", "noopener");
      window.setTimeout(() => URL.revokeObjectURL(fileUrl), 60_000);
    } catch (error) {
      showToast(error.message, true);
    }
  }

  function openMemoryForm() {
    const backdrop = document.createElement("div");
    backdrop.className = "modal-backdrop";
    backdrop.innerHTML = `
      <section class="modal" role="dialog" aria-modal="true" aria-labelledby="memory-form-title">
        <div class="modal-heading">
          <div><p class="eyebrow">Un momento para guardar</p><h2 id="memory-form-title">Nuevo recuerdo</h2><p>Elige cómo quieres conservarlo.</p></div>
          <button class="button button-icon" id="close-modal" type="button" aria-label="Cerrar">×</button>
        </div>
        <form id="memory-form">
          <div class="field">
            <label for="memory-title">Título</label>
            <input id="memory-title" name="tituloRecuerdo" maxlength="180" placeholder="Un domingo en familia…" required>
          </div>
          <div class="field">
            <label for="memory-type">Tipo de recuerdo</label>
            <select id="memory-type" name="tipo" required>
              <option value="TEXTO">Historia escrita</option>
              <option value="AUDIO">Audio</option>
              <option value="IMAGEN">Fotografía</option>
            </select>
          </div>
          <div class="field" id="content-field">
            <label for="memory-content">Cuéntanos la historia</label>
            <textarea id="memory-content" name="contenido" maxlength="5000" placeholder="¿Qué pasó? ¿Quién estaba contigo? ¿Qué detalle te gustaría recordar?"></textarea>
          </div>
          <div class="field" id="file-field" hidden>
            <label for="memory-file">Elige un archivo</label>
            <input id="memory-file" name="archivo" type="file">
            <p class="file-help">Tamaño máximo: 20 MB. Formatos compatibles según tu servidor.</p>
          </div>
          <p class="form-error" id="memory-error" role="alert"></p>
          <div class="modal-footer">
            <button class="button button-soft" id="cancel-modal" type="button">Cancelar</button>
            <button class="button button-primary" type="submit">Guardar recuerdo <span aria-hidden="true">→</span></button>
          </div>
        </form>
      </section>`;
    document.body.append(backdrop);
    const close = () => backdrop.remove();
    backdrop.querySelector("#close-modal").addEventListener("click", close);
    backdrop.querySelector("#cancel-modal").addEventListener("click", close);
    backdrop.addEventListener("click", (event) => { if (event.target === backdrop) close(); });
    backdrop.querySelector("#memory-type").addEventListener("change", (event) => {
      const text = event.target.value === "TEXTO";
      backdrop.querySelector("#content-field").hidden = !text;
      backdrop.querySelector("#memory-content").required = text;
      backdrop.querySelector("#file-field").hidden = text;
      backdrop.querySelector("#memory-file").required = !text;
      backdrop.querySelector("#memory-file").accept = event.target.value === "AUDIO" ? "audio/*" : "image/*";
    });
    backdrop.querySelector("#memory-form").addEventListener("submit", (event) => submitMemory(event, backdrop));
    backdrop.querySelector("#memory-title").focus();
  }

  async function submitMemory(event, backdrop) {
    event.preventDefault();
    const form = event.currentTarget;
    const submit = form.querySelector('button[type="submit"]');
    const error = backdrop.querySelector("#memory-error");
    error.textContent = "";
    submit.disabled = true;
    const fields = new FormData(form);
    const title = fields.get("tituloRecuerdo").toString().trim();
    const type = fields.get("tipo").toString();
    const adultId = encodeURIComponent(activeAdult.idAdultoMayor);
    const prefix = isCaregiver()
      ? `/cuidadores/${encodeURIComponent(caregiverId)}/adultos-mayores/${adultId}/recuerdos`
      : `/adultos-mayores/${adultId}/recuerdos`;
    try {
      if (type === "TEXTO") {
        await request(`${prefix}/texto`, {
          method: "POST",
          body: JSON.stringify({ tituloRecuerdo: title, contenido: fields.get("contenido").toString().trim() })
        });
      } else {
        const upload = new FormData();
        upload.set("tituloRecuerdo", title);
        upload.set("archivo", fields.get("archivo"));
        await request(`${prefix}/${type.toLocaleLowerCase("es")}`, { method: "POST", body: upload });
      }
      backdrop.remove();
      showToast("Recuerdo guardado en tu biblioteca.");
      await loadMemories();
    } catch (err) {
      error.textContent = err.message || "No se pudo guardar el recuerdo.";
    } finally {
      if (submit.isConnected) submit.disabled = false;
    }
  }

  function changeProfile() {
    activeAdult = null;
    memories = [];
    if (isCaregiver()) {
      caregiverId = null;
      assignedAdults = [];
    } else {
      sessionStorage.removeItem("soulstory.adult");
    }
    render();
  }

  if (session && !isCaregiver()) {
    try {
      activeAdult = JSON.parse(sessionStorage.getItem("soulstory.adult") || "null");
    } catch {
      sessionStorage.removeItem("soulstory.adult");
    }
  }
  render();
})();
