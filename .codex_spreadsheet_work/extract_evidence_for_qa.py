from pathlib import Path
from zipfile import ZipFile
from PIL import Image, ImageDraw

xlsx = Path(r"D:\Ltrinh TBDD\StateTestApp\outputs\01a0397b-ae26-7f12-a82e-0c59a49790af\Testcase_State_FINAL.xlsx")
qa_dir = xlsx.parent / "verification_media"
qa_dir.mkdir(parents=True, exist_ok=True)

with ZipFile(xlsx) as archive:
    media = sorted(
        [name for name in archive.namelist() if name.startswith("xl/media/")],
        key=lambda value: (len(value), value),
    )
    extracted = []
    for index, name in enumerate(media, start=1):
        target = qa_dir / f"evidence_{index:02}.png"
        target.write_bytes(archive.read(name))
        extracted.append(target)

thumb_w, thumb_h = 360, 210
label_h = 22
cols = 4
rows = (len(extracted) + cols - 1) // cols
canvas = Image.new("RGB", (thumb_w * cols, (thumb_h + label_h) * rows), "white")
draw = ImageDraw.Draw(canvas)

for index, item in enumerate(extracted):
    image = Image.open(item).convert("RGB")
    image.thumbnail((thumb_w - 8, thumb_h - 8))
    x = (index % cols) * thumb_w
    y = (index // cols) * (thumb_h + label_h)
    draw.text((x + 5, y + 3), item.stem, fill="black")
    canvas.paste(image, (x + (thumb_w - image.width) // 2, y + label_h + (thumb_h - image.height) // 2))
    draw.rectangle((x, y, x + thumb_w - 1, y + thumb_h + label_h - 1), outline="#94A3B8", width=1)

canvas.save(qa_dir / "Evidence_Media_Contact_Sheet.png")
print(f"Extracted {len(extracted)} media images")
