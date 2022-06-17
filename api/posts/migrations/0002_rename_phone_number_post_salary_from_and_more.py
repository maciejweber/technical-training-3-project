# Generated by Django 4.0.5 on 2022-06-17 13:20

from django.db import migrations, models


class Migration(migrations.Migration):

    dependencies = [
        ('categories', '0001_initial'),
        ('posts', '0001_initial'),
    ]

    operations = [
        migrations.RenameField(
            model_name='post',
            old_name='phone_number',
            new_name='salary_from',
        ),
        migrations.RenameField(
            model_name='post',
            old_name='price',
            new_name='salary_to',
        ),
        migrations.RemoveField(
            model_name='post',
            name='image',
        ),
        migrations.AddField(
            model_name='post',
            name='contact_email',
            field=models.CharField(default=1, max_length=100),
            preserve_default=False,
        ),
        migrations.RemoveField(
            model_name='post',
            name='category',
        ),
        migrations.AddField(
            model_name='post',
            name='category',
            field=models.ManyToManyField(to='categories.category'),
        ),
        migrations.AlterField(
            model_name='post',
            name='content',
            field=models.TextField(max_length=1000),
        ),
        migrations.DeleteModel(
            name='PostImage',
        ),
    ]
