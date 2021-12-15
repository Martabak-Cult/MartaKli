MartaKli is a command line tool to help you generate your NFT pfp project.

Current features:

- Generate batch of images using different layers and corresponding metadata
- Merge different directories of images/metadata into one


# Getting started

## Config

The command line tool can be configured with a `config.json` file.

`{ 
"layers": {
"Background": "assets/Background",
"Body": "assets/Body",
"Head": "assets/Head"
},
"resolution": {
"width" : 1800,
"height" : 1800
},
"metadata": {
"namePrefix": "Name #",
"description": "My super NFT project"
}
}
`

- `layers` represents the different layers your image will be generated with. In this example `Background` will be the first layers and will user image files located in `assets/Background`. In the metadata, the corresponding property name will be `Background`. Layers are ordered following the JSON order.
- `resolution` represents the generated image resolution
- `metadata` 
  - `namePrefix` is the prefix that will be added in the property `name` of the metadata. In this example, the image with tokenId 2 will have a `name` property with the value `Name #2`.
  - `description` is the description property value in the metadata.

## Generation

`./martakli gen --output batch1 --amount 100`

Will generate 100 images and their metadata in the batch1 directory

## Merge

`./martakli merge batch1 batch2 batch3`

Will merge all images within the folders batch1 batch2 and batch3 into one folder. Token IDs are updated accordingly.
