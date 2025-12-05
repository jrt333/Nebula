const DEFAULT_MAIL_SUBJECT = '欢迎来到大鸟转转转-星塔旅人！';
const DEFAULT_MAIL_BODY = '欢迎来到大鸟转转转-星塔旅人！QQ交流群：531390126，祝你游戏愉快！';

const COMMANDS = {
  '玩家管理': [
    {
      id: 'account',
      name: 'account create/delete',
      description: '创建或删除账户，可选预留 UID。',
      fields: [
        { key: 'action', label: '操作', type: 'select', options: ['create', 'delete'], required: true },
        { key: 'email', label: '邮箱（可选）', placeholder: 'email@example.com', type: 'text' },
        { key: 'uid', label: '预留 UID（可选）', placeholder: '100001', type: 'text' },
      ],
      build: (v) => `account ${v.action}${v.email ? ` ${v.email}` : ''}${v.uid ? ` ${v.uid}` : ''}`,
    },
    {
      id: 'setlevel',
      name: 'setlevel',
      description: '设置玩家等级。',
      fields: [
        { key: 'level', label: '等级', type: 'number', placeholder: '60', required: true },
      ],
      build: (v) => `setlevel ${v.level}`,
    },
  ],
  '物品管理': [
    {
      id: 'give',
      name: 'give',
      description: '通过 !give 向目标发放指定物品。',
      fields: [
        { key: 'item', label: '物品', type: 'datalist-items', placeholder: 'ID 或名称', required: true },
        { key: 'qty', label: '数量', type: 'number', placeholder: '1', required: true },
      ],
      build: (v) => `give ${v.item} x${v.qty}`,
    },
    {
      id: 'giveall',
      name: 'giveall',
      description: '按类别发放物品，可附加天赋/技能等级。',
      fields: [
        { key: 'type', label: '类别', type: 'select', options: ['characters', 'discs', 'materials'], required: true },
        { key: 'talent', label: '天赋等级 (t)', type: 'number', placeholder: '10' },
        { key: 'skill', label: '技能等级 (s)', type: 'number', placeholder: '10' },
      ],
      build: (v) => {
        const t = v.talent ? ` t${v.talent}` : '';
        const s = v.skill ? ` s${v.skill}` : '';
        return `giveall ${v.type}${t}${s}`;
      },
    },
    {
      id: 'cleanBatch',
      name: 'clean (批量)',
      description: '按类型清理物品或资源。',
      fields: [
        { key: 'target', label: '清理范围', type: 'select', options: ['all', 'items', 'resources'], required: true },
      ],
      build: (v) => `clean ${v.target}`,
    },
    {
      id: 'cleanSingle',
      name: 'clean (单个)',
      description: '清理指定 ID 的物品或资源。',
      fields: [
        { key: 'id', label: 'ID', type: 'text', placeholder: '物品或资源 ID', required: true },
      ],
      build: (v) => `clean ${v.id}`,
    },
  ],
  '角色管理': [
    {
      id: 'characterAll',
      name: 'character all',
      description: '给所有角色设置等级/突破/技能好感。',
      fields: [
        { key: 'lv', label: '等级 (lv)', type: 'number', placeholder: '60' },
        { key: 'a', label: '突破 (a)', type: 'number', placeholder: '6' },
        { key: 's', label: '技能等级 (s)', type: 'number', placeholder: '10' },
        { key: 't', label: '战技等级 (t)', type: 'number', placeholder: '10' },
        { key: 'f', label: '好感等级 (f)', type: 'number', placeholder: '10' },
      ],
      build: (v) => joinSegments(['character all', prefixNumber('lv', v.lv), prefixNumber('a', v.a), prefixNumber('s', v.s), prefixNumber('t', v.t), prefixNumber('f', v.f)]),
    },
    {
      id: 'characterOne',
      name: 'character',
      description: '针对单个角色设置等级/突破/技能好感。',
      fields: [
        { key: 'char', label: '角色', type: 'datalist-characters', placeholder: '角色 ID 或名称', required: true },
        { key: 'lv', label: '等级 (lv)', type: 'number', placeholder: '60' },
        { key: 'a', label: '突破 (a)', type: 'number', placeholder: '6' },
        { key: 's', label: '技能等级 (s)', type: 'number', placeholder: '10' },
        { key: 't', label: '战技等级 (t)', type: 'number', placeholder: '10' },
        { key: 'f', label: '好感等级 (f)', type: 'number', placeholder: '10' },
      ],
      build: (v) => joinSegments(['character', v.char, prefixNumber('lv', v.lv), prefixNumber('a', v.a), prefixNumber('s', v.s), prefixNumber('t', v.t), prefixNumber('f', v.f)]),
    },
  ],
  '秘纹管理': [
    {
      id: 'discAll',
      name: 'disc all',
      description: '批量设置秘纹等级/突破/共鸣。',
      fields: [
        { key: 'lv', label: '等级 (lv)', type: 'number', placeholder: '60' },
        { key: 'a', label: '突破 (a)', type: 'number', placeholder: '6' },
        { key: 'c', label: '共鸣 (c)', type: 'number', placeholder: '10' },
      ],
      build: (v) => joinSegments(['disc all', prefixNumber('lv', v.lv), prefixNumber('a', v.a), prefixNumber('c', v.c)]),
    },
    {
      id: 'discOne',
      name: 'disc',
      description: '为指定秘纹设置等级/突破/共鸣。',
      fields: [
        { key: 'disc', label: '秘纹 ID', type: 'text', placeholder: '秘纹 ID', required: true },
        { key: 'lv', label: '等级 (lv)', type: 'number', placeholder: '60' },
        { key: 'a', label: '突破 (a)', type: 'number', placeholder: '6' },
        { key: 'c', label: '共鸣 (c)', type: 'number', placeholder: '10' },
      ],
      build: (v) => joinSegments(['disc', v.disc, prefixNumber('lv', v.lv), prefixNumber('a', v.a), prefixNumber('c', v.c)]),
    },
  ],
  '服务器控制': [
    {
      id: 'reload',
      name: 'reload',
      description: '重载服务器配置。',
      fields: [],
      build: () => 'reload',
    },
  ],
  '构建系统': [
    {
      id: 'build',
      name: 'build',
      description: '一次性构建角色/秘纹/潜能/旋律 ID 列表（用空格分隔）。',
      fields: [
        { key: 'chars', label: '角色 ID 列表', type: 'text', placeholder: '例如：1001 1002' },
        { key: 'discs', label: '秘纹 ID 列表', type: 'text', placeholder: '201 202' },
        { key: 'potentials', label: '潜能 ID 列表', type: 'text', placeholder: '301 302' },
        { key: 'melodies', label: '旋律 ID 列表', type: 'text', placeholder: '401 402' },
      ],
      build: (v) => joinSegments(['build', v.chars, v.discs, v.potentials, v.melodies]),
    },
  ],
};

const state = {
  attachments: [],
  items: [],
  characters: [],
  currentCommand: COMMANDS['玩家管理'][0],
};

const els = {
  category: document.getElementById('categorySelect'),
  command: document.getElementById('commandSelect'),
  fields: document.getElementById('commandFields'),
  description: document.getElementById('commandDescription'),
  preview: document.getElementById('preview'),
  log: document.getElementById('logArea'),
  itemList: document.getElementById('itemList'),
  btnExecute: document.getElementById('btnExecute'),
  btnCopy: document.getElementById('btnCopy'),
  btnSaveConfig: document.getElementById('btnSaveConfig'),
  btnClearLog: document.getElementById('btnClearLog'),
  btnSendMail: document.getElementById('btnSendMail'),
  btnAddAttachment: document.getElementById('btnAddAttachment'),
  btnClearAttachment: document.getElementById('btnClearAttachment'),
  attachmentRow: document.getElementById('attachmentChips'),
  mailPreview: document.getElementById('mailPreview'),
};

function init() {
  loadConfig();
  populateCategories();
  loadDataLists();
  bindEvents();
  renderCommandsForCategory(Object.keys(COMMANDS)[0]);
  updatePreview();
  updateMailPreview();
}

function bindEvents() {
  els.category.addEventListener('change', (e) => renderCommandsForCategory(e.target.value));
  els.command.addEventListener('change', () => setCurrentCommand());
  els.btnExecute.addEventListener('click', () => executeCurrentCommand());
  els.btnCopy.addEventListener('click', () => copyPreview());
  els.btnSaveConfig.addEventListener('click', () => saveConfig());
  els.btnClearLog.addEventListener('click', () => (els.log.innerHTML = ''));
  els.btnSendMail.addEventListener('click', () => sendMail());
  els.btnAddAttachment.addEventListener('click', () => addAttachment());
  els.btnClearAttachment.addEventListener('click', () => {
    state.attachments = [];
    renderAttachments();
  });
}

function populateCategories() {
  Object.keys(COMMANDS).forEach((cat) => {
    const opt = document.createElement('option');
    opt.value = cat;
    opt.textContent = cat;
    els.category.appendChild(opt);
  });
}

function renderCommandsForCategory(category) {
  els.command.innerHTML = '';
  COMMANDS[category].forEach((cmd, idx) => {
    const opt = document.createElement('option');
    opt.value = cmd.id;
    opt.textContent = cmd.name;
    if (idx === 0) opt.selected = true;
    els.command.appendChild(opt);
  });
  setCurrentCommand();
}

function setCurrentCommand() {
  const category = els.category.value;
  const id = els.command.value;
  state.currentCommand = COMMANDS[category].find((c) => c.id === id);
  renderFields(state.currentCommand);
  updatePreview();
}

function renderFields(command) {
  els.description.textContent = command.description || '';
  els.fields.innerHTML = '';

  command.fields.forEach((field) => {
    const wrapper = document.createElement('label');
    wrapper.className = 'field';
    wrapper.innerHTML = `<span>${field.label}${field.required ? ' *' : ''}</span>`;

    let input;
    const baseId = `field-${command.id}-${field.key}`;

    switch (field.type) {
      case 'select':
        input = document.createElement('select');
        field.options.forEach((opt) => {
          const o = document.createElement('option');
          o.value = opt;
          o.textContent = opt;
          input.appendChild(o);
        });
        break;
      case 'number':
        input = document.createElement('input');
        input.type = 'number';
        input.min = '0';
        break;
      case 'datalist-items':
        input = document.createElement('input');
        input.setAttribute('list', 'itemList');
        break;
      case 'datalist-characters':
        input = document.createElement('input');
        input.setAttribute('list', 'charList');
        ensureCharacterDatalist();
        break;
      default:
        input = document.createElement('input');
        input.type = 'text';
    }

    input.id = baseId;
    input.placeholder = field.placeholder || '';
    input.dataset.required = field.required ? 'true' : 'false';

    input.addEventListener('input', () => updatePreview());
    wrapper.appendChild(input);
    els.fields.appendChild(wrapper);
  });
}

function collectFieldValues(command) {
  const values = {};
  let hasError = false;

  command.fields.forEach((f) => {
    const val = document.getElementById(`field-${command.id}-${f.key}`).value.trim();
    if (f.required && !val) {
      hasError = true;
    }
    values[f.key] = normalizeDatalistValue(val);
  });

  return { values, hasError };
}

function normalizeDatalistValue(val) {
  if (!val) return '';
  if (val.includes(' - ')) {
    return val.split(' - ')[0];
  }
  return val;
}

function prefixNumber(prefix, value) {
  if (!value) return '';
  return `${prefix}${value}`;
}

function joinSegments(segments) {
  return segments.filter((s) => s && String(s).trim() !== '').join(' ');
}

function buildCommandText() {
  const cmd = state.currentCommand;
  const { values, hasError } = collectFieldValues(cmd);
  if (hasError) return null;
  const base = cmd.build(values).trim();
  const uid = document.getElementById('inputUid').value.trim();
  return uid ? `!${base} @${uid}` : `!${base}`;
}

async function executeCurrentCommand() {
  const commandText = buildCommandText();
  if (!commandText) {
    pushLog('请填写必填字段后再执行。', true);
    return;
  }
  await sendCommand(commandText);
}

async function sendCommand(commandText) {
  const server = document.getElementById('inputServer').value.trim();
  const token = document.getElementById('inputToken').value.trim();

  if (!server || !token) {
    pushLog('请先填写服务器地址与远程密钥。', true);
    return;
  }

  try {
    pushLog(`> ${commandText}`);
    const res = await fetch(`${server}/api/command`, {
      method: 'POST',
      headers: { 'Content-Type': 'application/json' },
      body: JSON.stringify({ token, command: commandText.replace(/^!/, '') }),
    });

    const text = await res.text();
    if (res.ok) {
      pushLog(text || '执行成功');
    } else {
      pushLog(`${res.status} - ${text}`, true);
    }
  } catch (err) {
    pushLog(`请求失败: ${err.message}`, true);
  }
}

function pushLog(message, isError = false) {
  const entry = document.createElement('p');
  entry.className = 'log-entry';
  const time = new Date().toLocaleTimeString();
  entry.innerHTML = `<strong>[${time}]</strong> <span class="${isError ? 'error' : ''}">${message}</span>`;
  els.log.prepend(entry);
}

function updatePreview() {
  const cmdText = buildCommandText();
  els.preview.textContent = cmdText || '-';
}

async function copyPreview() {
  const cmdText = buildCommandText();
  if (!cmdText) return;
  await navigator.clipboard.writeText(cmdText);
  pushLog('已复制命令到剪贴板。');
}

function saveConfig() {
  const config = {
    server: document.getElementById('inputServer').value.trim(),
    token: document.getElementById('inputToken').value.trim(),
    uid: document.getElementById('inputUid').value.trim(),
  };
  localStorage.setItem('nebula-web-admin', JSON.stringify(config));
  pushLog('配置已保存。');
}

function loadConfig() {
  const raw = localStorage.getItem('nebula-web-admin');
  if (!raw) return;
  try {
    const cfg = JSON.parse(raw);
    document.getElementById('inputServer').value = cfg.server || '';
    document.getElementById('inputToken').value = cfg.token || '';
    document.getElementById('inputUid').value = cfg.uid || '';
  } catch (e) {
    console.warn('配置读取失败', e);
  }
}

async function loadDataLists() {
  try {
    const itemsJson = window.ITEMS_DATA || null;
    const charsJson = window.CHARACTERS_DATA || null;

    if (!itemsJson || !charsJson) {
      throw new Error('静态数据未加载');
    }

    state.items = (itemsJson.items || []).map((item) => ({ id: item.id, name: item.title }));
    state.characters = (charsJson.characters || []).map((c) => ({ id: c.id, name: c.title }));
    renderItemDatalist();
    ensureCharacterDatalist();
  } catch (e) {
    pushLog('物品/角色数据加载失败，仍可手动输入 ID。', true);
  }
}

function renderItemDatalist() {
  els.itemList.innerHTML = '';
  state.items.forEach((item) => {
    const opt = document.createElement('option');
    opt.value = `${item.id} - ${item.name}`;
    els.itemList.appendChild(opt);
  });
}

function ensureCharacterDatalist() {
  if (document.getElementById('charList')) return;
  const dl = document.createElement('datalist');
  dl.id = 'charList';
  state.characters.forEach((c) => {
    const opt = document.createElement('option');
    opt.value = `${c.id} - ${c.name}`;
    dl.appendChild(opt);
  });
  document.body.appendChild(dl);
}

function addAttachment() {
  const raw = document.getElementById('itemInput').value.trim();
  const qty = parseInt(document.getElementById('itemQty').value, 10) || 1;
  if (!raw) return;

  const id = normalizeDatalistValue(raw);
  const label = findItemName(id) || raw;
  state.attachments.push({ id, qty, label });
  document.getElementById('itemInput').value = '';
  document.getElementById('itemQty').value = '1';
  renderAttachments();
  updateMailPreview();
}

function findItemName(id) {
  const match = state.items.find((i) => String(i.id) === id);
  return match ? `${match.id} - ${match.name}` : '';
}

function renderAttachments() {
  els.attachmentRow.innerHTML = '';
  state.attachments.forEach((att, idx) => {
    const chip = document.createElement('div');
    chip.className = 'chip';
    chip.innerHTML = `${att.label || att.id} x${att.qty} <button aria-label="删除" data-idx="${idx}">×</button>`;
    chip.querySelector('button').addEventListener('click', () => {
      state.attachments.splice(idx, 1);
      renderAttachments();
      updateMailPreview();
    });
    els.attachmentRow.appendChild(chip);
  });
}

function buildMailCommand() {
  const subject = document.getElementById('mailSubject').value.trim() || DEFAULT_MAIL_SUBJECT;
  const body = document.getElementById('mailBody').value.trim() || DEFAULT_MAIL_BODY;
  const attachments = state.attachments.map((a) => `${a.id} x${a.qty}`).join(' ');
  const base = attachments ? `mail "${subject}" "${body}" ${attachments}` : `mail "${subject}" "${body}"`;
  const uid = document.getElementById('inputUid').value.trim();
  return uid ? `!${base} @${uid}` : `!${base}`;
}

async function sendMail() {
  const cmd = buildMailCommand();
  updateMailPreview();
  await sendCommand(cmd);
}

function updateMailPreview() {
  els.mailPreview.textContent = buildMailCommand();
}

init();
