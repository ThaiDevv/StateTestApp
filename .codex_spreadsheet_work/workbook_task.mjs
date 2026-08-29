import fs from "node:fs/promises";
import path from "node:path";
import { FileBlob, SpreadsheetFile } from "@oai/artifact-tool";
import JSZip from "jszip";

const sourcePath = "D:/Ltrinh TBDD/Testcase.xlsx";
const outputDir = "D:/Ltrinh TBDD/StateTestApp/outputs/01a0397b-ae26-7f12-a82e-0c59a49790af";
const outputPath = path.join(outputDir, "Testcase_State_FINAL.xlsx");
const renderDir = path.join(outputDir, "verification_renders");

if (process.argv.includes("--inspect-current")) {
  const current = await SpreadsheetFile.importXlsx(await FileBlob.load(outputPath));
  await fs.mkdir(renderDir, { recursive: true });
  const info = await current.inspect({
    kind: "sheet,drawing,table",
    maxChars: 30000,
    tableMaxRows: 12,
    tableMaxCols: 8,
  });
  await fs.writeFile(path.join(outputDir, "current_before_edit.ndjson"), info.ndjson, "utf8");
  for (const tcName of ["TC09", "TC14", "TC19", "TC24"]) {
    const preview = await current.render({
      sheetName: tcName,
      range: "A1:AF55",
      scale: 0.25,
      format: "png",
    });
    await fs.writeFile(path.join(renderDir, `before_${tcName}.png`), new Uint8Array(await preview.arrayBuffer()));
  }
  console.log(info.ndjson);
  process.exit(0);
}

if (process.argv.includes("--update-completed")) {
  const current = await SpreadsheetFile.importXlsx(await FileBlob.load(outputPath));
  const summarySheet = current.worksheets.getItem("Test Cases");
  const completed = [
    {
      id: 9,
      row: 13,
      result: "Kết quả: CÒN - tên Test State, count=3, switch Bật; Activity được tạo lại với isRestored=true.\nNguyên nhân: rememberSaveable đăng ký state với SavedStateRegistry. onSaveInstanceState lưu các giá trị vào Bundle trước khi Activity bị hủy và Composition mới đọc lại Bundle khi Activity được tạo lại.",
    },
    {
      id: 14,
      row: 20,
      result: "Kết quả: MẤT - tên trống, count=0, switch Tắt; ViewModel cũ #185322183 được cleared và ViewModel mới #105839229 được tạo.\nNguyên nhân: Don't keep activities hủy Activity khi ứng dụng xuống nền với isChangingConfigurations=false, nên ViewModelStore bị xóa. ViewModel thuần chỉ giữ dữ liệu trong RAM và không tự phục hồi từ savedInstanceState.",
    },
    {
      id: 19,
      row: 27,
      result: "Kết quả: CÒN - Test State, 3, Bật; ViewModel cũ #1906321 được cleared và ViewModel mới #252658283 nhận lại dữ liệu.\nNguyên nhân: SavedStateHandle ghi state vào SavedInstanceState Bundle. Khi Activity được tạo lại với isRestored=true, ViewModel mới đọc Restored values từ Bundle nên UI được phục hồi.",
    },
    {
      id: 24,
      row: 34,
      result: "Kết quả: CÒN - Test State, 3, Bật sau khi Activity bị hủy và tạo lại.\nNguyên nhân: Preferences DataStore đã ghi dữ liệu vào file trên Internal Storage. Activity mới thu thập lại Flow, đọc name/count/choice từ disk và cập nhật UI; việc hủy Activity chỉ xóa state trong RAM.",
    },
  ];

  for (const item of completed) {
    const tcName = `TC${String(item.id).padStart(2, "0")}`;
    const evidenceSheet = current.worksheets.getItem(tcName);
    evidenceSheet.unmergeCells("B3:H7");
    evidenceSheet.getRange("B3:H7").clear({ applyTo: "all" });
    summarySheet.getRange(`E${item.row}`).values = [[item.result]];
    summarySheet.getRange(`F${item.row}`).formulas = [[
      `=HYPERLINK("#'${tcName}'!A1","Xem ảnh tại sheet ${tcName}")`,
    ]];
    summarySheet.getRange(`G${item.row}`).values = [["Pass"]];
    summarySheet.getRange(`F${item.row}`).format = {
      font: { color: "#1D4ED8", bold: false, size: 10 },
      horizontalAlignment: "center",
      verticalAlignment: "center",
      wrapText: true,
      fill: "#EFF6FF",
      borders: { preset: "all", style: "thin", color: "#D9E2F3" },
    };
    summarySheet.getRange(`G${item.row}`).format = {
      font: { color: "#16A34A", bold: true, size: 10 },
      horizontalAlignment: "center",
      verticalAlignment: "center",
      wrapText: true,
      fill: "#F0FDF4",
      borders: { preset: "all", style: "thin", color: "#D9E2F3" },
    };
  }

  summarySheet.getRange("A37:G37").unmerge();
  summarySheet.getRange("A37:G37").clear({ applyTo: "all" });
  summarySheet.getRange("A37:G37").merge();
  summarySheet.getRange("A37:G37").values = [[
    "TỔNG KẾT: 25 test case; đã có đủ 25 ảnh minh chứng; 25/25 test case đã thực hiện và đạt.",
  ]];
  summarySheet.getRange("A37:G37").format = {
    fill: "#DCFCE7",
    font: { bold: true, color: "#166534", size: 10 },
    wrapText: true,
    verticalAlignment: "center",
    borders: { preset: "outside", style: "medium", color: "#16A34A" },
  };
  summarySheet.getRange("A37:G37").format.rowHeight = 34;

  const tempPath = path.join(outputDir, ".Testcase_State_FINAL.tmp.xlsx");
  const exported = await SpreadsheetFile.exportXlsx(current);
  await exported.save(tempPath);

  const zip = await JSZip.loadAsync(await fs.readFile(tempPath));
  let normalizedHyperlinks = 0;
  for (const fileName of Object.keys(zip.files).filter((name) => /^xl\/worksheets\/sheet\d+\.xml$/.test(name))) {
    const entry = zip.file(fileName);
    if (!entry) continue;
    let xml = await entry.async("string");
    xml = xml.replace(
      /<x:c([^>]*?) t="e"([^>]*)><x:f>(HYPERLINK\("[^"]*","([^"]*)"\))<\/x:f><x:v>[^<]*<\/x:v><\/x:c>/g,
      (_match, beforeType, afterType, formula, friendlyName) => {
        normalizedHyperlinks += 1;
        return `<x:c${beforeType} t="str"${afterType}><x:f>${formula}</x:f><x:v>${friendlyName}</x:v></x:c>`;
      },
    );
    zip.file(fileName, xml);
  }
  const workbookXmlEntry = zip.file("xl/workbook.xml");
  if (!workbookXmlEntry) throw new Error("Missing xl/workbook.xml");
  let workbookXml = await workbookXmlEntry.async("string");
  if (workbookXml.includes("<x:calcPr")) {
    workbookXml = workbookXml.replace(/<x:calcPr[^>]*\/>/, '<x:calcPr calcId="191029" calcMode="auto" fullCalcOnLoad="1" forceFullCalc="1"/>');
  } else {
    workbookXml = workbookXml.replace(
      "</x:workbook>",
      '<x:calcPr calcId="191029" calcMode="auto" fullCalcOnLoad="1" forceFullCalc="1"/></x:workbook>',
    );
  }
  zip.file("xl/workbook.xml", workbookXml);
  await fs.writeFile(tempPath, await zip.generateAsync({ type: "nodebuffer", compression: "DEFLATE" }));
  await fs.rename(tempPath, outputPath);

  const checkBook = await SpreadsheetFile.importXlsx(await FileBlob.load(outputPath));
  const keyRange = await checkBook.inspect({
    kind: "table",
    sheetId: "Test Cases",
    range: "A1:G38",
    include: "values,formulas",
    tableMaxRows: 45,
    tableMaxCols: 7,
    tableMaxCellChars: 260,
    maxChars: 32000,
  });
  await fs.writeFile(path.join(outputDir, "verification_values.ndjson"), keyRange.ndjson, "utf8");
  const formulaErrors = await checkBook.inspect({
    kind: "match",
    searchTerm: "#REF!|#DIV/0!|#VALUE!|#NAME\\?|#N/A",
    options: { useRegex: true, maxResults: 300 },
    summary: "final formula error scan",
  });
  await fs.writeFile(path.join(outputDir, "verification_errors.ndjson"), formulaErrors.ndjson, "utf8");
  const drawings = await checkBook.inspect({ kind: "drawing", maxChars: 30000 });
  await fs.writeFile(path.join(outputDir, "verification_drawings.ndjson"), drawings.ndjson, "utf8");
  const summaryPreview = await checkBook.render({
    sheetName: "Test Cases",
    range: "A1:G38",
    scale: 0.65,
    format: "png",
  });
  await fs.writeFile(path.join(renderDir, "Test_Cases.png"), new Uint8Array(await summaryPreview.arrayBuffer()));
  for (const tcName of ["TC09", "TC14", "TC19", "TC24"]) {
    const preview = await checkBook.render({
      sheetName: tcName,
      range: "A1:AF55",
      scale: 0.25,
      format: "png",
    });
    await fs.writeFile(path.join(renderDir, `${tcName}.png`), new Uint8Array(await preview.arrayBuffer()));
  }
  console.log(JSON.stringify({ outputPath, updated: completed.map((item) => item.id), normalizedHyperlinks }));
  process.exit(0);
}

if (process.argv.includes("--replace-datastore-evidence")) {
  const screenshotMap = new Map([
    ["TC21", "C:/Users/LENOVO/AppData/Local/Temp/codex-clipboard-e43e1b73-8ece-4812-acc7-816d127c61e9.png"],
    ["TC22", "C:/Users/LENOVO/AppData/Local/Temp/codex-clipboard-91d64ab5-fcf6-448e-900e-899b644d9566.png"],
    ["TC23", "C:/Users/LENOVO/AppData/Local/Temp/codex-clipboard-75b0b1c5-c59b-44b3-8fe1-f0d75c269157.png"],
    ["TC25", "C:/Users/LENOVO/AppData/Local/Temp/codex-clipboard-4060e9e9-220a-45c2-91d7-fbf3c443909e.png"],
  ]);
  const current = await SpreadsheetFile.importXlsx(await FileBlob.load(outputPath));
  const summarySheet = current.worksheets.getItem("Test Cases");

  for (const [tcName, screenshotPath] of screenshotMap) {
    const bytes = await fs.readFile(screenshotPath);
    if (bytes.toString("ascii", 1, 4) !== "PNG") throw new Error(`${screenshotPath} is not PNG`);
    const widthPx = bytes.readUInt32BE(16);
    const heightPx = bytes.readUInt32BE(20);
    const sheet = current.worksheets.getItem(tcName);
    sheet.deleteAllDrawings();
    sheet.images.add({
      dataUrl: `data:image/png;base64,${bytes.toString("base64")}`,
      anchor: {
        from: { row: 0, col: 1 },
        extent: { widthPx, heightPx },
      },
    });
  }

  const updatedResults = new Map([
    [31, "Kết quả: CÒN - Test State!, 3, Bật sau recomposition; Logcat có Saved/Read và Recomposition với name='Test State!', count=3, choice=true.\nNguyên nhân: DataStore ghi các thay đổi vào file Preferences và Flow phát giá trị mới cho UI; recomposition không xóa dữ liệu trên bộ nhớ trong."],
    [32, "Kết quả: CÒN - Test State!, 3, Bật sau khi xoay màn hình; Activity được tạo lại với isRestored=true và DataStore đọc lại đủ name/count/choice từ disk.\nNguyên nhân: Configuration Change chỉ tạo lại Activity/Composition; file DataStore vẫn tồn tại và Flow của màn hình mới tiếp tục đọc dữ liệu đã lưu."],
    [33, "Kết quả: CÒN - Test State!, 3, Bật sau khi Back về Home rồi mở lại DataStore; màn hình mới khởi tạo state mặc định trước khi đọc lại dữ liệu từ disk.\nNguyên nhân: Back loại màn hình khỏi NavBackStack nhưng không xóa file DataStore; lần điều hướng mới thu thập Flow và nhận lại các giá trị đã lưu."],
    [35, "Kết quả: CÒN - Test State!, 3, Bật sau khi kill process; Activity mới có isRestored=true và Logcat đọc lại name/count/choice từ disk.\nNguyên nhân: kill process xóa RAM nhưng không xóa file Preferences DataStore trong Internal Storage; tiến trình mới đọc lại file và phục hồi UI."],
  ]);
  for (const [row, result] of updatedResults) summarySheet.getRange(`E${row}`).values = [[result]];

  summarySheet.getRange("A38:G38").unmerge();
  summarySheet.getRange("A38:G38").clear({ applyTo: "all" });

  const tempPath = path.join(outputDir, ".Testcase_State_FINAL.tmp.xlsx");
  const exported = await SpreadsheetFile.exportXlsx(current);
  await exported.save(tempPath);

  const zip = await JSZip.loadAsync(await fs.readFile(tempPath));
  let normalizedHyperlinks = 0;
  for (const fileName of Object.keys(zip.files).filter((name) => /^xl\/worksheets\/sheet\d+\.xml$/.test(name))) {
    const entry = zip.file(fileName);
    if (!entry) continue;
    let xml = await entry.async("string");
    xml = xml.replace(
      /<x:c([^>]*?) t="e"([^>]*)><x:f>(HYPERLINK\("[^"]*","([^"]*)"\))<\/x:f><x:v>[^<]*<\/x:v><\/x:c>/g,
      (_match, beforeType, afterType, formula, friendlyName) => {
        normalizedHyperlinks += 1;
        return `<x:c${beforeType} t="str"${afterType}><x:f>${formula}</x:f><x:v>${friendlyName}</x:v></x:c>`;
      },
    );
    zip.file(fileName, xml);
  }
  const workbookXmlEntry = zip.file("xl/workbook.xml");
  if (!workbookXmlEntry) throw new Error("Missing xl/workbook.xml");
  let workbookXml = await workbookXmlEntry.async("string");
  if (workbookXml.includes("<x:calcPr")) {
    workbookXml = workbookXml.replace(/<x:calcPr[^>]*\/>/, '<x:calcPr calcId="191029" calcMode="auto" fullCalcOnLoad="1" forceFullCalc="1"/>');
  } else {
    workbookXml = workbookXml.replace(
      "</x:workbook>",
      '<x:calcPr calcId="191029" calcMode="auto" fullCalcOnLoad="1" forceFullCalc="1"/></x:workbook>',
    );
  }
  zip.file("xl/workbook.xml", workbookXml);
  await fs.writeFile(tempPath, await zip.generateAsync({ type: "nodebuffer", compression: "DEFLATE" }));
  await fs.rename(tempPath, outputPath);

  const checkBook = await SpreadsheetFile.importXlsx(await FileBlob.load(outputPath));
  const keyRange = await checkBook.inspect({
    kind: "table",
    sheetId: "Test Cases",
    range: "A29:G38",
    include: "values,formulas",
    tableMaxRows: 12,
    tableMaxCols: 7,
    tableMaxCellChars: 280,
    maxChars: 15000,
  });
  await fs.writeFile(path.join(outputDir, "verification_values.ndjson"), keyRange.ndjson, "utf8");
  const formulaErrors = await checkBook.inspect({
    kind: "match",
    searchTerm: "#REF!|#DIV/0!|#VALUE!|#NAME\\?|#N/A",
    options: { useRegex: true, maxResults: 300 },
    summary: "final formula error scan",
  });
  await fs.writeFile(path.join(outputDir, "verification_errors.ndjson"), formulaErrors.ndjson, "utf8");
  const drawings = await checkBook.inspect({ kind: "drawing", maxChars: 30000 });
  await fs.writeFile(path.join(outputDir, "verification_drawings.ndjson"), drawings.ndjson, "utf8");
  const summaryPreview = await checkBook.render({
    sheetName: "Test Cases",
    range: "A29:G37",
    scale: 0.9,
    format: "png",
  });
  await fs.writeFile(path.join(renderDir, "Test_Cases_DataStore.png"), new Uint8Array(await summaryPreview.arrayBuffer()));
  for (const tcName of screenshotMap.keys()) {
    const preview = await checkBook.render({
      sheetName: tcName,
      range: "A1:AF55",
      scale: 0.25,
      format: "png",
    });
    await fs.writeFile(path.join(renderDir, `${tcName}.png`), new Uint8Array(await preview.arrayBuffer()));
  }
  console.log(JSON.stringify({
    outputPath,
    replacedEvidence: [...screenshotMap.keys()],
    normalizedHyperlinks,
  }));
  process.exit(0);
}

const workbook = await SpreadsheetFile.importXlsx(await FileBlob.load(sourcePath));
if (process.argv.includes("--help-hyperlink")) {
  console.log(workbook.help("hyperlink", { include: "index,examples,notes", maxChars: 12000 }).ndjson);
  process.exit(0);
}
const sheets = workbook.worksheets.items;
if (sheets.length < 26) throw new Error(`Expected 26 sheets, found ${sheets.length}`);

const summary = sheets[0];
summary.name = "Test Cases";
summary.deleteAllDrawings();
for (const table of [...summary.tables.items]) table.delete();
summary.getRange("A1:AF230").clear({ applyTo: "all" });

const evidencePresent = new Set([
  1, 2, 3, 4, 5, 6, 7, 8, 10, 11, 12, 13, 15, 16, 17, 18, 20, 21, 22, 23, 25,
]);

for (let i = 1; i <= 25; i += 1) {
  const sheet = sheets[i];
  const tcName = `TC${String(i).padStart(2, "0")}`;
  sheet.name = tcName;
  sheet.showGridLines = false;
  sheet.getRange("A1").values = [[tcName]];
  sheet.getRange("A1").format = {
    fill: "#203447",
    font: { bold: true, color: "#FFFFFF", size: 12 },
    horizontalAlignment: "center",
    verticalAlignment: "center",
  };
  sheet.getRange("A1").format.columnWidth = 12;
  sheet.getRange("A1").format.rowHeight = 26;
  sheet.getRange("A2").formulas = [["=HYPERLINK(\"#'Test Cases'!A1\",\"Quay lại bảng Test Cases\")"]];
  sheet.getRange("A2").format = {
    font: { color: "#1D4ED8", bold: true, size: 10 },
    horizontalAlignment: "center",
    verticalAlignment: "center",
    wrapText: true,
  };

  if (!evidencePresent.has(i)) {
    sheet.getRange("B3:H7").merge();
    sheet.getRange("B3:H7").values = [[
      `CHƯA CÓ ẢNH MINH CHỨNG ${tcName}\nTest case này chưa được thực hiện. Hãy thay bằng ảnh chụp toàn màn hình gồm Code + thiết bị + Logcat + chữ ký nhóm.`,
    ]];
    sheet.getRange("B3:H7").format = {
      fill: "#FFF4CE",
      font: { bold: true, color: "#7A4E00", size: 12 },
      wrapText: true,
      horizontalAlignment: "center",
      verticalAlignment: "center",
      borders: { preset: "outside", style: "medium", color: "#D6A100" },
    };
  }
}

const baseInput = (file, action) =>
  `1) Mở ${file}, Logcat lọc tag:STATE_TEST và hiển thị chữ ký nhóm.\n` +
  `2) Nhập \"Test State\", bấm +1 ba lần, bật switch.\n` +
  `3) ${action}\n` +
  `4) Quan sát STATE HIỆN TẠI, Logcat và chụp toàn màn hình.`;

const cases = [
  {
    id: 1, group: "REMEMBER TEST CASES", name: "remember - Recomposition",
    description: "Kiểm tra state của remember khi Composable bị recomposition.",
    input: baseInput("RememberScreen.kt", "Nhập thêm dấu ! vào cuối tên để gây recomposition."),
    result: "Kết quả: CÒN - tên Test State!, count=3, switch Bật.\nNguyên nhân: remember lưu giá trị trong Slot Table của Composition; recomposition chỉ chạy lại phần UI cần thiết, không phá hủy Composition nên state vẫn còn.",
  },
  {
    id: 2, group: "REMEMBER TEST CASES", name: "remember - Xoay màn hình",
    description: "Kiểm tra remember sau Configuration Change.",
    input: baseInput("RememberScreen.kt", "Xoay thiết bị từ dọc sang ngang."),
    result: "Kết quả: MẤT - tên trống, count=0, switch Tắt.\nNguyên nhân: xoay màn hình làm Activity và Composition bị tạo lại; remember chỉ nằm trong RAM của Composition cũ và không được ghi vào Bundle.",
  },
  {
    id: 3, group: "REMEMBER TEST CASES", name: "remember - Back rồi vào lại",
    description: "Kiểm tra remember khi màn hình bị pop khỏi NavBackStack.",
    input: baseInput("RememberScreen.kt", "Bấm Back về Home rồi mở lại màn hình remember."),
    result: "Kết quả: MẤT - state trở về mặc định.\nNguyên nhân: Back loại NavBackStackEntry và Composable khỏi Composition; vùng nhớ do remember quản lý bị giải phóng, lần vào sau tạo state mới.",
  },
  {
    id: 4, group: "REMEMBER TEST CASES", name: "remember - Tạo lại Activity",
    description: "Kiểm tra remember khi bật Don't keep activities.",
    input: baseInput("RememberScreen.kt", "Bật Don't keep activities, bấm Home, chờ Activity bị hủy rồi mở lại từ Recent Apps."),
    result: "Kết quả: MẤT - state trở về mặc định.\nNguyên nhân: hệ thống hủy Activity khi app xuống nền, kéo theo Composition bị hủy; remember không có cơ chế lưu bền vững để phục hồi.",
  },
  {
    id: 5, group: "REMEMBER TEST CASES", name: "remember - Kill process",
    description: "Kiểm tra remember sau khi tiến trình ứng dụng bị kill.",
    input: baseInput("RememberScreen.kt", "Bấm Home, chạy adb shell am kill com.nhom.statetestapp rồi mở lại từ Recent Apps."),
    result: "Kết quả: MẤT - state trở về mặc định.\nNguyên nhân: kill process xóa toàn bộ RAM của ứng dụng; remember không ghi state vào Bundle hoặc file nên không thể khôi phục.",
  },
  {
    id: 6, group: "REMEMBERSAVEABLE TEST CASES", name: "rememberSaveable - Recomposition",
    description: "Kiểm tra rememberSaveable khi Composable bị recomposition.",
    input: baseInput("RememberSaveableScreen.kt", "Nhập thêm dấu ! vào cuối tên để gây recomposition."),
    result: "Kết quả: CÒN - dữ liệu không đổi ngoài ký tự vừa nhập.\nNguyên nhân: recomposition không hủy Composition; rememberSaveable vẫn dùng state đã đăng ký trong Composition hiện tại.",
  },
  {
    id: 7, group: "REMEMBERSAVEABLE TEST CASES", name: "rememberSaveable - Xoay màn hình",
    description: "Kiểm tra khả năng phục hồi state qua Configuration Change.",
    input: baseInput("RememberSaveableScreen.kt", "Xoay thiết bị sang ngang."),
    result: "Kết quả: CÒN - Test State, 3, Bật.\nNguyên nhân: trước khi Activity bị tạo lại, rememberSaveable lưu các kiểu dữ liệu hỗ trợ vào SavedInstanceState Bundle và phục hồi chúng cho Composition mới.",
  },
  {
    id: 8, group: "REMEMBERSAVEABLE TEST CASES", name: "rememberSaveable - Back rồi vào lại",
    description: "Kiểm tra rememberSaveable khi NavBackStackEntry bị loại bỏ.",
    input: baseInput("RememberSaveableScreen.kt", "Bấm Back về Home rồi mở lại rememberSaveable."),
    result: "Kết quả: MẤT - state trở về mặc định.\nNguyên nhân: Back pop NavBackStackEntry; SavedStateRegistryOwner gắn với entry đó bị hủy nên lần điều hướng mới không còn Bundle của màn hình cũ.",
  },
  {
    id: 9, group: "REMEMBERSAVEABLE TEST CASES", name: "rememberSaveable - Tạo lại Activity",
    description: "Kiểm tra rememberSaveable với Don't keep activities.",
    input: baseInput("RememberSaveableScreen.kt", "Bật Don't keep activities, bấm Home, chờ Activity bị hủy rồi mở lại từ Recent Apps."),
    result: "Kỳ vọng: CÒN - Test State, 3, Bật.\nNguyên nhân: onSaveInstanceState lưu state vào Bundle trước khi Activity bị hủy và rememberSaveable đọc lại Bundle khi Activity được tạo lại. Chưa có kết quả thực tế.",
  },
  {
    id: 10, group: "REMEMBERSAVEABLE TEST CASES", name: "rememberSaveable - Kill process",
    description: "Kiểm tra rememberSaveable sau process death có SavedInstanceState.",
    input: baseInput("RememberSaveableScreen.kt", "Bấm Home, chờ onSaveInstanceState, chạy adb kill rồi mở lại từ Recent Apps."),
    result: "Kết quả: CÒN - Test State, 3, Bật và isRestored=true.\nNguyên nhân: Bundle đã được System Server giữ trước khi tiến trình chết; Activity mới nhận lại savedInstanceState và rememberSaveable phục hồi dữ liệu.",
  },
  {
    id: 11, group: "VIEWMODEL TEST CASES", name: "ViewModel - Recomposition",
    description: "Kiểm tra state trong ViewModel khi UI recomposition.",
    input: baseInput("ViewModelScreen.kt", "Ghi mã ViewModel rồi nhập thêm dấu ! để gây recomposition."),
    result: "Kết quả: CÒN - dữ liệu và mã ViewModel giữ nguyên.\nNguyên nhân: ViewModel nằm ngoài Composition và giữ state trong instance riêng; recomposition chỉ đọc lại state để dựng UI.",
  },
  {
    id: 12, group: "VIEWMODEL TEST CASES", name: "ViewModel - Xoay màn hình",
    description: "Kiểm tra ViewModel qua Configuration Change.",
    input: baseInput("ViewModelScreen.kt", "Ghi mã ViewModel, xoay thiết bị rồi so sánh mã và dữ liệu."),
    result: "Kết quả: CÒN - state và mã ViewModel giữ nguyên.\nNguyên nhân: ViewModelStore được giữ qua Configuration Change bằng non-configuration instance, nên Activity mới nhận lại ViewModel cũ.",
  },
  {
    id: 13, group: "VIEWMODEL TEST CASES", name: "ViewModel - Back rồi vào lại",
    description: "Kiểm tra ViewModel khi ViewModelStoreOwner của màn hình bị xóa.",
    input: baseInput("ViewModelScreen.kt", "Ghi mã ViewModel, bấm Back rồi mở lại màn hình và so sánh mã."),
    result: "Kết quả: MẤT - ViewModel cũ được cleared, instance mới có state mặc định.\nNguyên nhân: Back pop NavBackStackEntry là ViewModelStoreOwner; store bị clear nên ViewModel cũ gọi onCleared().",
  },
  {
    id: 14, group: "VIEWMODEL TEST CASES", name: "ViewModel - Tạo lại Activity",
    description: "Kiểm tra ViewModel với Don't keep activities.",
    input: baseInput("ViewModelScreen.kt", "Bật Don't keep activities, bấm Home, mở lại từ Recent Apps và so sánh mã ViewModel."),
    result: "Kỳ vọng theo kế hoạch: CÒN nếu ViewModelStore được retained như một lần tái tạo Activity.\nLưu ý nguyên nhân cần xác minh: một số thiết bị coi đây là Activity bị hủy vĩnh viễn và gọi onCleared(), khi đó state sẽ MẤT. Chưa có kết quả thực tế.",
  },
  {
    id: 15, group: "VIEWMODEL TEST CASES", name: "ViewModel - Kill process",
    description: "Kiểm tra ViewModel sau khi process bị kill.",
    input: baseInput("ViewModelScreen.kt", "Ghi mã ViewModel, bấm Home, chạy adb kill rồi mở lại từ Recent Apps."),
    result: "Kết quả: MẤT - instance mới, tên trống, count=0, switch Tắt.\nNguyên nhân: ViewModel chỉ lưu dữ liệu trong RAM; process death xóa instance và ViewModel thuần không tự ghi dữ liệu vào SavedState Bundle.",
  },
  {
    id: 16, group: "SAVEDSTATEHANDLE TEST CASES", name: "SavedStateHandle - Recomposition",
    description: "Kiểm tra state của SavedStateHandle khi UI recomposition.",
    input: baseInput("SavedStateScreen.kt", "Ghi mã ViewModel rồi nhập thêm dấu ! để gây recomposition."),
    result: "Kết quả: CÒN - Test State!, 3, Bật; instance không đổi.\nNguyên nhân: state nằm trong ViewModel/SavedStateHandle, ngoài Composition; recomposition không tạo lại ViewModel.",
  },
  {
    id: 17, group: "SAVEDSTATEHANDLE TEST CASES", name: "SavedStateHandle - Xoay màn hình",
    description: "Kiểm tra SavedStateHandle qua Configuration Change.",
    input: baseInput("SavedStateScreen.kt", "Ghi mã ViewModel, xoay màn hình rồi kiểm tra state và mã."),
    result: "Kết quả: CÒN - dữ liệu và instance giữ nguyên.\nNguyên nhân: ViewModel sống qua Configuration Change; SavedStateHandle tiếp tục cung cấp cùng state cho Activity/Composition mới.",
  },
  {
    id: 18, group: "SAVEDSTATEHANDLE TEST CASES", name: "SavedStateHandle - Back rồi vào lại",
    description: "Kiểm tra SavedStateHandle khi NavBackStackEntry bị pop.",
    input: baseInput("SavedStateScreen.kt", "Ghi mã ViewModel, bấm Back về Home rồi mở lại màn hình."),
    result: "Kết quả: MẤT - ViewModel cũ cleared, ViewModel mới nhận name rỗng, count=0, choice=false.\nNguyên nhân: Back loại hẳn owner khỏi back stack; entry mới không có saved state của entry đã bị xóa.",
  },
  {
    id: 19, group: "SAVEDSTATEHANDLE TEST CASES", name: "SavedStateHandle - Tạo lại Activity",
    description: "Kiểm tra SavedStateHandle với Don't keep activities.",
    input: baseInput("SavedStateScreen.kt", "Bật Don't keep activities, bấm Home, chờ Activity bị hủy rồi mở lại từ Recent Apps."),
    result: "Kỳ vọng: CÒN - dữ liệu được phục hồi dù ViewModel có thể là instance mới.\nNguyên nhân: SavedStateHandle ghi các giá trị vào SavedInstanceState Bundle và nhận lại chúng khi Activity được phục hồi. Chưa có kết quả thực tế.",
  },
  {
    id: 20, group: "SAVEDSTATEHANDLE TEST CASES", name: "SavedStateHandle - Kill process",
    description: "Kiểm tra phục hồi state sau process death.",
    input: baseInput("SavedStateScreen.kt", "Ghi mã ViewModel, bấm Home, chờ lưu Bundle, chạy adb kill rồi mở lại từ Recent Apps."),
    result: "Kết quả: CÒN - ViewModel mới nhưng Restored values vẫn là Test State, 3, true.\nNguyên nhân: SavedStateHandle là cầu nối giữa ViewModel và SavedInstanceState; OS giữ Bundle và tiêm lại dữ liệu cho ViewModel mới.",
  },
  {
    id: 21, group: "DATASTORE TEST CASES", name: "DataStore - Recomposition",
    description: "Kiểm tra state lưu bằng Preferences DataStore khi UI recomposition.",
    input: baseInput("DataStoreScreen.kt", "Nhập thêm dấu ! và chờ log Saved/Read/Recomposition."),
    result: "Kết quả: CÒN - Test State!, 3, Bật.\nNguyên nhân: giá trị được ghi vào file Preferences DataStore và được UI quan sát qua Flow; recomposition không xóa dữ liệu trên bộ nhớ trong.",
  },
  {
    id: 22, group: "DATASTORE TEST CASES", name: "DataStore - Xoay màn hình",
    description: "Kiểm tra DataStore qua Configuration Change.",
    input: baseInput("DataStoreScreen.kt", "Xoay thiết bị rồi đối chiếu UI với các log Read ... from disk."),
    result: "Kết quả: CÒN - Test State!, 3, Bật.\nNguyên nhân: Activity/Composition mới thu thập lại Flow và đọc các giá trị đã lưu trong file DataStore, nên Configuration Change không làm mất state.",
  },
  {
    id: 23, group: "DATASTORE TEST CASES", name: "DataStore - Back rồi vào lại",
    description: "Kiểm tra DataStore khi màn hình bị pop khỏi NavBackStack.",
    input: baseInput("DataStoreScreen.kt", "Bấm Back về Home rồi mở lại DataStore; kiểm tra log đọc từ disk."),
    result: "Kết quả: CÒN - Test State!, 3, Bật.\nNguyên nhân: Back chỉ hủy màn hình và Composition; file DataStore nằm trên Internal Storage không phụ thuộc NavBackStackEntry nên màn hình mới đọc lại được dữ liệu.",
  },
  {
    id: 24, group: "DATASTORE TEST CASES", name: "DataStore - Tạo lại Activity",
    description: "Kiểm tra DataStore với Don't keep activities.",
    input: baseInput("DataStoreScreen.kt", "Bật Don't keep activities, bấm Home, chờ Activity bị hủy rồi mở lại từ Recent Apps."),
    result: "Kỳ vọng: CÒN - state được đọc lại từ file.\nNguyên nhân: việc hủy Activity chỉ xóa đối tượng trong RAM; Preferences DataStore đã ghi dữ liệu xuống Internal Storage nên Activity mới có thể đọc lại. Chưa có kết quả thực tế.",
  },
  {
    id: 25, group: "DATASTORE TEST CASES", name: "DataStore - Kill process",
    description: "Kiểm tra dữ liệu DataStore sau khi tiến trình bị kill.",
    input: baseInput("DataStoreScreen.kt", "Bấm Home, chạy adb shell am kill com.nhom.statetestapp rồi mở lại từ Recent Apps."),
    result: "Kết quả: CÒN - Test State!, 3, Bật; Logcat đọc lại name/count/choice từ disk.\nNguyên nhân: DataStore lưu dữ liệu bền vững trong file của Internal Storage; kill process chỉ xóa RAM, không xóa file ứng dụng.",
  },
];

const headers = ["No", "Name", "Description", "Input / Steps", "Kết quả & Nguyên nhân", "Test Image / Evidence", "Status"];
const rows = [];
const headerRows = [];
const groupRows = [];
const caseRows = [];
let excelRow = 1;

for (const groupName of [
  "REMEMBER TEST CASES",
  "REMEMBERSAVEABLE TEST CASES",
  "VIEWMODEL TEST CASES",
  "SAVEDSTATEHANDLE TEST CASES",
  "DATASTORE TEST CASES",
]) {
  rows.push(headers);
  headerRows.push(excelRow);
  excelRow += 1;
  rows.push([groupName, null, null, null, null, null, null]);
  groupRows.push(excelRow);
  excelRow += 1;
  for (const tc of cases.filter((item) => item.group === groupName)) {
    rows.push([
      tc.id,
      tc.name,
      tc.description,
      tc.input,
      tc.result,
      evidencePresent.has(tc.id) ? `Xem ảnh tại sheet TC${String(tc.id).padStart(2, "0")}` : `Chưa có ảnh TC${String(tc.id).padStart(2, "0")}`,
      evidencePresent.has(tc.id) ? "Pass" : "Chưa test",
    ]);
    caseRows.push({ row: excelRow, id: tc.id, hasEvidence: evidencePresent.has(tc.id) });
    excelRow += 1;
  }
}

summary.getRange(`A1:G${rows.length}`).values = rows;
for (const item of caseRows) {
  const tcName = `TC${String(item.id).padStart(2, "0")}`;
  const linkText = item.hasEvidence
    ? `Xem ảnh tại sheet ${tcName}`
    : `Mở sheet ${tcName} - chưa có ảnh`;
  summary.getRange(`F${item.row}`).formulas = [[
    `=HYPERLINK(\"#'${tcName}'!A1\",\"${linkText}\")`,
  ]];
}
summary.showGridLines = false;
summary.freezePanes.freezeRows(1);

summary.getRange(`A1:G${rows.length}`).format = {
  font: { color: "#1F2937", size: 10 },
  verticalAlignment: "top",
  wrapText: true,
  borders: { preset: "all", style: "thin", color: "#D9E2F3" },
};
summary.getRange(`A1:A${rows.length}`).format.horizontalAlignment = "center";
summary.getRange(`F1:G${rows.length}`).format.horizontalAlignment = "center";

for (const row of headerRows) {
  summary.getRange(`A${row}:G${row}`).format = {
    fill: "#203447",
    font: { bold: true, color: "#FFFFFF", size: 10 },
    horizontalAlignment: "center",
    verticalAlignment: "center",
    wrapText: true,
    borders: { preset: "all", style: "thin", color: "#B8C7D9" },
  };
  summary.getRange(`A${row}:G${row}`).format.rowHeight = 28;
}

for (const row of groupRows) {
  summary.getRange(`A${row}:G${row}`).merge();
  summary.getRange(`A${row}:G${row}`).format = {
    fill: "#D6E0F0",
    font: { bold: true, color: "#334155", size: 11 },
    horizontalAlignment: "left",
    verticalAlignment: "center",
    borders: { preset: "outside", style: "thin", color: "#B8C7D9" },
  };
  summary.getRange(`A${row}:G${row}`).format.rowHeight = 24;
}

for (const item of caseRows) {
  summary.getRange(`A${item.row}:G${item.row}`).format.rowHeight = 92;
  summary.getRange(`F${item.row}`).format = {
    font: { color: item.hasEvidence ? "#1D4ED8" : "#92400E", bold: false, size: 10 },
    horizontalAlignment: "center",
    verticalAlignment: "center",
    wrapText: true,
    fill: item.hasEvidence ? "#EFF6FF" : "#FFF7ED",
    borders: { preset: "all", style: "thin", color: "#D9E2F3" },
  };
  summary.getRange(`G${item.row}`).format = {
    font: { color: item.hasEvidence ? "#16A34A" : "#B45309", bold: true, size: 10 },
    horizontalAlignment: "center",
    verticalAlignment: "center",
    wrapText: true,
    fill: item.hasEvidence ? "#F0FDF4" : "#FFF7ED",
    borders: { preset: "all", style: "thin", color: "#D9E2F3" },
  };
}

summary.getRange("A1:A40").format.columnWidth = 7;
summary.getRange("B1:B40").format.columnWidth = 31;
summary.getRange("C1:C40").format.columnWidth = 42;
summary.getRange("D1:D40").format.columnWidth = 52;
summary.getRange("E1:E40").format.columnWidth = 64;
summary.getRange("F1:F40").format.columnWidth = 25;
summary.getRange("G1:G40").format.columnWidth = 14;

const noteStart = rows.length + 2;
summary.getRange(`A${noteStart}:G${noteStart}`).merge();
summary.getRange(`A${noteStart}:G${noteStart}`).values = [[
  "TỔNG KẾT: 25 test case; 21 test case có ảnh minh chứng; 4 test case chưa thực hiện: TC09, TC14, TC19, TC24.",
]];
summary.getRange(`A${noteStart}:G${noteStart}`).format = {
  fill: "#FFF4CE",
  font: { bold: true, color: "#7A4E00", size: 10 },
  wrapText: true,
  verticalAlignment: "center",
  borders: { preset: "outside", style: "medium", color: "#D6A100" },
};
summary.getRange(`A${noteStart}:G${noteStart}`).format.rowHeight = 34;

summary.getRange(`A${noteStart + 1}:G${noteStart + 1}`).merge();
summary.getRange(`A${noteStart + 1}:G${noteStart + 1}`).values = [[
  "LƯU Ý MINH CHỨNG: TC21-TC23 đã chứng minh đúng chức năng DataStore nhưng ảnh hiện có đang mở nhầm SavedStateScreen.kt; nên chụp lại với DataStoreScreen.kt trước khi nộp.",
]];
summary.getRange(`A${noteStart + 1}:G${noteStart + 1}`).format = {
  fill: "#FEE2E2",
  font: { bold: true, color: "#991B1B", size: 10 },
  wrapText: true,
  verticalAlignment: "center",
  borders: { preset: "outside", style: "medium", color: "#DC2626" },
};
summary.getRange(`A${noteStart + 1}:G${noteStart + 1}`).format.rowHeight = 42;

summary.getRange(`G3:G${rows.length}`).dataValidation = {
  rule: { type: "list", values: ["Pass", "Chưa test"] },
};

await fs.mkdir(outputDir, { recursive: true });
await fs.mkdir(renderDir, { recursive: true });

const output = await SpreadsheetFile.exportXlsx(workbook);
await output.save(outputPath);

// artifact-tool preserves the HYPERLINK formulas but cannot calculate their
// cached display value. Normalize only those formula cells and force Excel to
// recalculate on open so the internal sheet jumps are immediately clickable.
const zip = await JSZip.loadAsync(await fs.readFile(outputPath));
let normalizedHyperlinks = 0;
for (const fileName of Object.keys(zip.files).filter((name) => /^xl\/worksheets\/sheet\d+\.xml$/.test(name))) {
  const entry = zip.file(fileName);
  if (!entry) continue;
  let xml = await entry.async("string");
  xml = xml.replace(
    /<x:c([^>]*?) t="e"([^>]*)><x:f>(HYPERLINK\("[^"]*","([^"]*)"\))<\/x:f><x:v>[^<]*<\/x:v><\/x:c>/g,
    (_match, beforeType, afterType, formula, friendlyName) => {
      normalizedHyperlinks += 1;
      return `<x:c${beforeType} t="str"${afterType}><x:f>${formula}</x:f><x:v>${friendlyName}</x:v></x:c>`;
    },
  );
  zip.file(fileName, xml);
}

const workbookXmlEntry = zip.file("xl/workbook.xml");
if (!workbookXmlEntry) throw new Error("Missing xl/workbook.xml");
let workbookXml = await workbookXmlEntry.async("string");
if (!workbookXml.includes("calcPr")) {
  workbookXml = workbookXml.replace(
    "</x:workbook>",
    '<x:calcPr calcId="191029" calcMode="auto" fullCalcOnLoad="1" forceFullCalc="1"/></x:workbook>',
  );
}
zip.file("xl/workbook.xml", workbookXml);
await fs.writeFile(outputPath, await zip.generateAsync({ type: "nodebuffer", compression: "DEFLATE" }));

if (normalizedHyperlinks !== 50) {
  throw new Error(`Expected 50 hyperlink formulas, normalized ${normalizedHyperlinks}`);
}

const finalBook = await SpreadsheetFile.importXlsx(await FileBlob.load(outputPath));
const keyRange = await finalBook.inspect({
  kind: "table",
  sheetId: "Test Cases",
  range: `A1:G${noteStart + 1}`,
  include: "values,formulas",
  tableMaxRows: 45,
  tableMaxCols: 7,
  tableMaxCellChars: 220,
  maxChars: 30000,
});
await fs.writeFile(path.join(outputDir, "verification_values.ndjson"), keyRange.ndjson, "utf8");

const formulaErrors = await finalBook.inspect({
  kind: "match",
  searchTerm: "#REF!|#DIV/0!|#VALUE!|#NAME\\?|#N/A",
  options: { useRegex: true, maxResults: 300 },
  summary: "final formula error scan",
});
await fs.writeFile(path.join(outputDir, "verification_errors.ndjson"), formulaErrors.ndjson, "utf8");

const drawings = await finalBook.inspect({ kind: "drawing", maxChars: 30000 });
await fs.writeFile(path.join(outputDir, "verification_drawings.ndjson"), drawings.ndjson, "utf8");

const summaryPreview = await finalBook.render({
  sheetName: "Test Cases",
  range: `A1:G${noteStart + 1}`,
  scale: 0.65,
  format: "png",
});
await fs.writeFile(path.join(renderDir, "Test_Cases.png"), new Uint8Array(await summaryPreview.arrayBuffer()));

for (let i = 1; i <= 25; i += 1) {
  const tcName = `TC${String(i).padStart(2, "0")}`;
  const preview = await finalBook.render({
    sheetName: tcName,
    range: evidencePresent.has(i) ? "A1:AF55" : "A1:J10",
    scale: 0.25,
    format: "png",
  });
  await fs.writeFile(path.join(renderDir, `${tcName}.png`), new Uint8Array(await preview.arrayBuffer()));
}

console.log(JSON.stringify({
  outputPath,
  testCases: cases.length,
  evidence: evidencePresent.size,
  pending: 25 - evidencePresent.size,
  hyperlinks: normalizedHyperlinks,
}));
